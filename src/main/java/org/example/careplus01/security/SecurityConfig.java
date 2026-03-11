package org.example.careplus01.security;

import org.example.careplus01.filters.JwtAuthenticationFilter;
import org.example.careplus01.filters.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);

        http
                .csrf(csrf -> csrf.disable())  // we don't need csrf since we are working with jwt not stateful auth
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session auth needed working with stateless auth only
                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/h2-console/**", "/refreshToken").permitAll() //  setting these urls publicly accessible ( no auth needed)
//                        .anyRequest().authenticated()
//                )
                .authorizeHttpRequests(auth -> auth
                        // Public routes - Thymeleaf view controllers
                        .requestMatchers("/", "/login-page", "/dashboard").permitAll()

                        // Public routes - Authentication
                        .requestMatchers("/login", "/refreshToken").permitAll()

                        // Public routes - H2 Console
                        .requestMatchers("/h2-console/**").permitAll()

                        // Public routes - Static resources (Fixed to match your root files)
                        .requestMatchers("/*.css", "/*.js", "/images/**", "/favicon.ico").permitAll()

                        // Explicitly Protect API routes
                        .requestMatchers("/api/patients/**").authenticated()
                        .requestMatchers("/users/**", "/roles/**", "/add-users", "/add-roles", "/add-role-to-user").authenticated()
                        .requestMatchers("/profile", "/admin/**").authenticated()

                        // Default MUST be authenticated for safety
                        .anyRequest().authenticated()
                )
                //end
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) //
                )
                .addFilter(jwtAuthenticationFilter) // add auth filter
                .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);// handles logins

        return http.build(); //  call the controller then validates jwt on every single request
    }

    @Bean // manages the auth process
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean // coordinates the user details service and password encoder
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean // used by jwt auth filter to validate user credentials
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}