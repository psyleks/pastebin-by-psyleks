package mvc.config;

import mvc.security.CustomAuthenticationFailureHandler;
import mvc.security.PasswordService;
import mvc.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/registration", "/static/**", "/activate/*", "/login*")

                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe.key("abobaSec1"))
                .logout(LogoutConfigurer::permitAll)
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/main/{uniqueId}")
                )
                .headers(headers -> headers
                        .contentSecurityPolicy(
                                "default-src 'self'; " +
                                "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://challenges.cloudflare.com https://cdnjs.cloudflare.com https://cdn.jsdelivr.net https://code.jquery.com; " +
                                "style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com https://cdn.jsdelivr.net; " +
                                "font-src 'self'; " +
                                "img-src 'self' data: https://challenges.cloudflare.com https://cdn.jsdelivr.net; " +
                                "connect-src 'self' https://challenges.cloudflare.com; " +
                                "object-src 'none'; " +
                                "frame-src 'self' https://challenges.cloudflare.com; " +
                                "base-uri 'self'; " +
                                "form-action 'self';"
                        )
                )
//                .headers(headers -> headers
//                        .contentSecurityPolicy(csp ->
//                                csp.policyDirectives(
//                                        "default-src 'self'; " +
//                                                "script-src 'self' " +
//                                                "https://challenges.cloudflare.com " +
//                                                "https://cdnjs.cloudflare.com " +
//                                                "https://cdn.jsdelivr.net " +
//                                                "https://code.jquery.com; " +
//                                                "style-src 'self' " +
//                                                "https://cdnjs.cloudflare.com " +
//                                                "https://cdn.jsdelivr.net " +
//                                                "sha256-daZRXo8ygYIJ5dDIT0TXUGTYgTjQFP9yrFvJ/TpsVPU=; " +
//                                                "font-src 'self'; " +
//                                                "img-src 'self' data: " +
//                                                "https://challenges.cloudflare.com " +
//                                                "https://cdn.jsdelivr.net; " +
//                                                "connect-src 'self' " +
//                                                "https://challenges.cloudflare.com; " +
//                                                "object-src 'none'; " +
//                                                "frame-src 'self' " +
//                                                "https://challenges.cloudflare.com; " +
//                                                "base-uri 'self'; " +
//                                                "form-action 'self'; "
//                                ).reportOnly()
//                        )
//                )
                .authenticationManager(authenticationManager(http.getSharedObject(AuthenticationManagerBuilder.class)));

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService).passwordEncoder(passwordService);
        return builder.build();
    }

}
