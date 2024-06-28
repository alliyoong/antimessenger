package com.khanh.antimessenger.configuration;

import com.khanh.antimessenger.constant.SecurityConstant;
import com.khanh.antimessenger.filter.CustomAccessDeniedHandler;
import com.khanh.antimessenger.filter.CustomForbiddenEntryPoint;
import com.khanh.antimessenger.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomForbiddenEntryPoint forbiddenEntryPoint;
    private final JwtAuthorizationFilter authorizationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Value("${application.front-end.url.admin}")
    private String frontEndUrlAdmin;

    @Value("${application.front-end.url.live-chat}")
    private String frontEndUrlLiveChat;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
//                .cors(Customizer.withDefaults())
//                .cors(cors -> cors.disable())
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                        request -> {
                            var cors = new CorsConfiguration();
                            cors.setAllowedHeaders(List.of("*"));
                            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            cors.setAllowedOrigins(List.of(frontEndUrlAdmin, frontEndUrlLiveChat, "*"));
                            return cors;
                        }
                ))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
                        .anyRequest().authenticated())
                .anonymous(httpSecurityAnonymousConfigurer -> httpSecurityAnonymousConfigurer.disable())
                .authenticationProvider(authenticationProvider)
                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint(forbiddenEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers(SecurityConstant.PUBLIC_URLS);
//    }

}
