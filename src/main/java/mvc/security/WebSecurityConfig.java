package mvc.security;

import mvc.service.PasswordService;
import mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordService passwordService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/registration", "static/**", "activate/*", "/loginError", "/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("abobaSec1")
                        .tokenRepository(redisTokenRepository())
                        .tokenValiditySeconds(60 * 60 * 24 * 7)) // Не работает??
                .logout(LogoutConfigurer::permitAll)
                .headers(headers -> headers
                        .contentSecurityPolicy("default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://www.cloudflare.com https://challenges.cloudflare.com" +
                                        "https://cdnjs.cloudflare.com https://cdn.jsdelivr.net https://code.jquery.com; " +
                                        "style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com https://cdn.jsdelivr.net; " +
                                        "font-src 'self'; " +
                                        "img-src 'self' data: https://www.cloudflare.com https://challenges.cloudflare.com https://cdn.jsdelivr.net; " +
                                        "connect-src 'self' https://www.cloudflare.com https://challenges.cloudflare.com; " +
                                        "object-src 'none'; " +
                                        "frame-src 'self' https://www.cloudflare.com https://challenges.cloudflare.com; " +
                                        "base-uri 'self'; " +
                                        "form-action 'self';"
                        )
                )
                .authenticationManager(authenticationManager(http.getSharedObject(AuthenticationManagerBuilder.class)));

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    private PersistentTokenRepository redisTokenRepository() {
        return new RedisPersistentTokenRepository(redisTemplate);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService).passwordEncoder(passwordService);
        return builder.build();
    }

}
