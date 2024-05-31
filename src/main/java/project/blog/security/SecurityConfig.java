package project.blog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//        (debug = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //변경 후
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/blog/write").authenticated() // 관리자 페이지는 인증 필요
                        .requestMatchers("/signin", "/signup", "/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스 접근 허용
                        .anyRequest().permitAll() // 그 외 모든 요청은 접근 허용
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/signin")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/signin?error")
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/signin")
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/")
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // X-Frame-Options 설정
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}