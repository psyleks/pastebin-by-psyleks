package mvc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Service
public class CachedIdGeneratorService {

    @Value("${id.length}")
    private int idLength;

    @Value("${cache.size}")
    private int cacheSize;

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final SecureRandom random = new SecureRandom();
    private final String redisKey = "uniqueIdCache";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public synchronized String getUniqueId() {
        try {
            String id = redisTemplate.opsForSet().pop(redisKey);
            if (id == null) {
                fillCache();
                id = redisTemplate.opsForSet().pop(redisKey);
            }
            return id;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации уникального идентификатора", e);
        }
    }

    private void fillCache() {
        Set<String> newIds = new HashSet<>();
        while (newIds.size() < cacheSize) {
            newIds.add(generateRandomId());
        }
        redisTemplate.opsForSet().add(redisKey, newIds.toArray(new String[0]));
    }

    private String generateRandomId() {
        StringBuilder idBuilder = new StringBuilder();
        for (int i = 0; i < idLength; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            idBuilder.append(CHAR_POOL.charAt(index));
        }
        return idBuilder.toString();
    }

    public boolean isUniqueConstraintViolation(DataIntegrityViolationException e) {
        String message = e.getRootCause().getMessage();
        return message != null && message.contains("unique_id");
    }

}
