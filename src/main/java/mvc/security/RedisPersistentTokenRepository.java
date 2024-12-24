package mvc.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisPersistentTokenRepository implements PersistentTokenRepository { // Не работает

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisPersistentTokenRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        String key = getKeyForUser(token.getUsername());
        redisTemplate.opsForValue().set(key, token);
        redisTemplate.expire(key,60, TimeUnit.SECONDS);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        return (PersistentRememberMeToken) redisTemplate.opsForValue().get(series);
    }

    @Override
    public void removeUserTokens(String username) {
        String key = getKeyForUser(username);
        redisTemplate.delete(key);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentRememberMeToken token = getTokenForSeries(series);
        if (token != null) {
            token = new PersistentRememberMeToken(token.getUsername(), series, tokenValue, lastUsed);
            redisTemplate.opsForValue().set(series, token);
            redisTemplate.expire(series, 60, TimeUnit.SECONDS);
        }
    }

    private String getKeyForUser(String username) {
        return "rememberMe:" + username;
    }
}
