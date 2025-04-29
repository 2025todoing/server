package hongik.Todoing.global.config;

import hongik.Todoing.domain.jwt.JwtAuthenticationFilter;
import hongik.Todoing.domain.jwt.JwtUtil;
import hongik.Todoing.global.util.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final String[] allowedUrls = {"/login",
            "/reissue", "/swagger-ui/**", "/swagger-resources/**", "v3/api-docs/**", "/auth/login/kakao/**"
    };

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        // cors 비활성화
        http
                .cors(cors -> cors
                        .configurationSource(CorsConfig.apiConfigurationSource()));

        // csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);

        // form 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // Session을 사용하지 않고, Stateless 서버를 만듬.
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 경로별 인가
        http.
                authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(allowedUrls).permitAll()
                                .requestMatchers("/user/**").authenticated()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                );
        // JWT 인증 필터 등록

        JwtAuthenticationFilter loginFilter = new JwtAuthenticationFilter(
                authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/login");
        http.
                addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
