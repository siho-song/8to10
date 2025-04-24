package com.eighttoten.config;

import com.eighttoten.auth.AuthRepository;
import com.eighttoten.filter.EmailPasswordAuthenticationFilter;
import com.eighttoten.filter.JwtAuthorizationFilter;
import com.eighttoten.handler.AuthFailureHandler;
import com.eighttoten.handler.AuthFilterExceptionHandler;
import com.eighttoten.handler.AuthSuccessHandler;
import com.eighttoten.provider.AuthProvider;
import com.eighttoten.service.MemberDetailsService;
import com.eighttoten.support.AuthAccessor;
import com.eighttoten.support.AuthAccessorImpl;
import com.eighttoten.support.BearerAuthorizationUtils;
import com.eighttoten.support.PasswordEncoder;
import com.eighttoten.support.PasswordEncoderImpl;
import com.eighttoten.support.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] STATIC_RESOURCES_LOCATION = new String[]{
            "/css/**", "/resources/**",
            "/static/**", "/images/**",
            "/js/**", "/public/**",
            "/favicon.ico"
    };

    private final AuthFilterExceptionHandler authFilterExceptionHandler;
    private final BearerAuthorizationUtils bearerAuthorizationUtils;
    private final MemberDetailsService memberDetailsService;
    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;

    @Value(value = "${cors.allowed-origin}")
    private String corsAllowedOrigin;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter,
            JwtAuthorizationFilter jwtAuthorizationFilter
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/signup/**", "/", "/error", "/renew", "/actuator/**", "/health",
                                "/notification/subscribe")
                        .permitAll()
                        .requestMatchers(STATIC_RESOURCES_LOCATION)
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessHandler(((request, response, authentication) ->
                                        response.setStatus(HttpServletResponse.SC_OK)
                                ))
                )
                .addFilterBefore(emailPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addExposedHeader("Authorization");
        config.setAllowedOrigins(Arrays.stream(corsAllowedOrigin.split(","))
                .map(String::trim)
                .toList());
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public EmailPasswordAuthenticationFilter customAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthSuccessHandler authSuccessHandler,
            AuthFailureHandler authFailureHandler,
            AuthFilterExceptionHandler authFilterExceptionHandler
    ) {
        EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter = new EmailPasswordAuthenticationFilter(
                authenticationManager, authFilterExceptionHandler);
        emailPasswordAuthenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        emailPasswordAuthenticationFilter.setAuthenticationFailureHandler(authFailureHandler);
        emailPasswordAuthenticationFilter.afterPropertiesSet();
        return emailPasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationProvider authProvider(PasswordEncoder passwordEncoder) {
        return new AuthProvider(memberDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authProvider) {
        return new ProviderManager(authProvider);
    }

    @Bean
    public AuthSuccessHandler customAuthSuccessHandler() {
        return new AuthSuccessHandler(tokenProvider, authRepository);
    }

    @Bean
    public AuthFailureHandler customAuthFailureHandler() {
        return new AuthFailureHandler(authFilterExceptionHandler);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(memberDetailsService, bearerAuthorizationUtils,
                tokenProvider, authFilterExceptionHandler);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new PasswordEncoderImpl(bCryptPasswordEncoder);
    }

    @Bean
    public AuthAccessor authAccessor(){
        return new AuthAccessorImpl(memberDetailsService);
    }
}