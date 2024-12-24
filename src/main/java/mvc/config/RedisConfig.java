package mvc.config;

import jakarta.mail.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;

@Configuration
@EnableRedisRepositories(keyspaceConfiguration = RedisConfig.MyKeySpaceConfiguration.class)
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    public static class MyKeySpaceConfiguration extends KeyspaceConfiguration { // Не работает

        @Override
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(Session.class, "session");
            keyspaceSettings.setTimeToLive(60L);
            return Collections.singletonList(keyspaceSettings);
        }
    }

}