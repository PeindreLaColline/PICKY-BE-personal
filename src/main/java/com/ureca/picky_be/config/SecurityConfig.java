package com.ureca.picky_be.config;

import com.ureca.picky_be.global.web.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] AUTH_WHITELIST = {
            //기본 세팅
            "/health",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",

            //로그인 없이도 접근 가능
            "/api/v1/oauth/*/user",
            "/api/v1/oauth/*/login",
            "/dummy",

            //GENRE
            "/api/v1/user/genres",

            //PLAYLIST
            "/api/v1/playlist/all",

            // EMAIL
            "/api/v1/email",

            //임시 허용
            "/tokentokentoken",

            //content 임시로 전체허용 이거 옮겨야함
            "/api/v1/content/image",
            "/api/v1/content/video",
            "/api/v1/content/profile",

    };

    private static final String[] AUTH_WHITELIST_GET = {
            // MOVIE
            "/api/v1/movie/**",

            // USER
            "/api/v1/user",
            "/api/v1/user/mypage/**",

            // NOTIFICATION
            "/api/v1/notification/connect",

            // BOARD
            "/api/v1/board",
            "/api/v1/board/*",

            // LINEREVIEW
            "/api/v1/linereview/**",

    };

    private static final String[] AUTH_USER = {
            //user
            "/api/v1/user",
            "/api/v1/user/movies-by-genres",
            "/api/v1/user/nickname-validation",

            //MOVIE
            "/api/v1/movie/*/like",
            "/api/v1/movie/search/**",

            // NOTIFICATION
            "/api/v1/notification/alert",
            "/api/v1/notification/test",

            // LINEREVIEW
            "/api/v1/linereview/create",
            "/api/v1/linereview/**",

            // EMAIL
            "/api/v1/email/**",

            // USER
            "/api/v1/user/search/**",

            // FOLLOW
            "/api/v1/follow/**",

    };

    private static final String[] AUTH_USER_PATCH = {
            //user
            "/api/v1/user",
            "/api/v1/user/mypage"
    };

    private static final String[] AUTH_ADMIN = {
            "/api/v1/admin/**"
    };

    private static final String[] AUTH_ADMIN_POST = {
            "/api/v1/movie/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, AUTH_WHITELIST_GET).permitAll()
                        .requestMatchers(AUTH_USER).hasAnyAuthority("USER", "CRITIC", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, AUTH_USER_PATCH).hasAnyAuthority("USER", "CRITIC", "ADMIN")
                        .requestMatchers(AUTH_ADMIN).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, AUTH_ADMIN_POST).hasAuthority("ADMIN")
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Value("${frontend.root_server}")
    private String frontRootUrl;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://www.picky-movie.com",
                "http://10.10.222.226:5173",
                frontRootUrl
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
