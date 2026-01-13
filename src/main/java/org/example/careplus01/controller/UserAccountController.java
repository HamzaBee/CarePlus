package org.example.careplus01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.example.careplus01.entity.AppRole;
import org.example.careplus01.entity.AppUser;
import org.example.careplus01.service.UserAccountService;
import org.example.careplus01.util.JwtUtil;
import org.example.careplus01.util.UserAndRoleBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class UserAccountController {

    private UserAccountService userAccountService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<AppUser> appUsers() {
        return userAccountService.listUsers();
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<AppRole> appRoles() {
        return userAccountService.listRoles();
    }

    @PostMapping("/add-users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public AppUser saveUser(@RequestBody AppUser appUser) {
        return userAccountService.addNewUserAccount(appUser);
    }

    @PostMapping("/add-roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return userAccountService.addNewRole(appRole);
    }

    @PostMapping("/add-role-to-user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public void addRoleToUser(@RequestBody UserAndRoleBuilder userAndRoleBuilder) {
        userAccountService.addRoleToUser(
                userAndRoleBuilder.getUsername(),
                userAndRoleBuilder.getRoleName()
        );
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public AppUser profile(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return userAccountService.loadUserByUsername(username);
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String authorizationHeader = request.getHeader(JwtUtil.HEADER_STRING);

        if (authorizationHeader != null && authorizationHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
            try {
                String refreshToken = authorizationHeader.substring(JwtUtil.TOKEN_PREFIX.length());

                // Verify and parse the refresh token
                var secretKey = Keys.hmacShaKeyFor(JwtUtil.SECRET.getBytes());
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(refreshToken)
                        .getPayload();

                String username = claims.getSubject();

                // Load user to get current roles
                AppUser appUser = userAccountService.loadUserByUsername(username);

                // Generate new access token
                String jwtAccessToken = Jwts.builder()
                        .subject(appUser.getUsername())
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + JwtUtil.EXPIRES_ACCESS_TOKEN))
                        .issuer(request.getRequestURL().toString())
                        .claim("roles", appUser.getRoles().stream()
                                .map(role -> role.getRoleName())
                                .collect(Collectors.toList()))
                        .signWith(secretKey)
                        .compact();

                // Return the new access token
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access-token", jwtAccessToken);
                tokens.put("refresh-token", refreshToken);

                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                response.setHeader("Error-Message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }


    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, String> adminDashboard() {
        return Map.of(
                "message", "Welcome to Admin Dashboard",
                "access", "ADMIN_ONLY"
        );
    }
}