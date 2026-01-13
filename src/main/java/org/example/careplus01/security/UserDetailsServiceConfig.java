package org.example.careplus01.security;

import org.example.careplus01.entity.AppUser;
import org.example.careplus01.service.UserAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
        return username -> {
            System.out.println("=== Loading User: " + username + " ===");

            AppUser appUser = userAccountService.loadUserByUsername(username);

            if (appUser == null) {
                System.out.println("User not found: " + username);
                throw new UsernameNotFoundException("User not found: " + username);
            }

            Collection<GrantedAuthority> authorities = appUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toList());

            System.out.println("User found: " + appUser.getUsername());
            System.out.println("Roles: " + authorities);

            return new User(
                    appUser.getUsername(),
                    appUser.getHashPassword(),
                    authorities
            );
        };
    }
}