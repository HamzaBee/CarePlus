package org.example.careplus01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.careplus01.DTO.AppRoleDTO;
import org.example.careplus01.DTO.AppUserCreateDTO;
import org.example.careplus01.DTO.AppUserDTO;
import org.example.careplus01.DTO.AppUserUpdateDTO;
import org.example.careplus01.exception.ExpiredRefreshTokenException;
import org.example.careplus01.exception.InvalidRefreshTokenException;
import org.example.careplus01.exception.ResourceNotFoundException;
import org.example.careplus01.service.UserAccountService;
import org.example.careplus01.serviceImpl.UserAccountServiceImpl;
import org.example.careplus01.util.JwtUtil;
import org.example.careplus01.util.UserAndRoleBuilder;
import org.springframework.http.ResponseEntity;
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

    private final UserAccountService userAccountService;
    private final UserAccountServiceImpl userAccountServiceImpl; // For entity access

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<AppUserDTO> appUsers() {
        return userAccountService.listUsers();
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<AppRoleDTO> appRoles() {
        return userAccountService.listRoles();
    }

    @PostMapping("/add-users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public AppUserDTO saveUser(@Valid @RequestBody AppUserCreateDTO createDTO) {
        return userAccountService.addNewUserAccount(createDTO);
    }

    @PutMapping("/users/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public AppUserDTO updateUser(
            @PathVariable String username,
            @Valid @RequestBody AppUserUpdateDTO updateDTO) {
        return userAccountService.updateUserAccount(username, updateDTO);
    }

    @PostMapping("/add-roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AppRoleDTO saveRole(@Valid @RequestBody AppRoleDTO roleDTO) {
        return userAccountService.addNewRole(roleDTO);
    }

    @PostMapping("/add-role-to-user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public void addRoleToUser(@RequestBody UserAndRoleBuilder userAndRoleBuilder) {
        userAccountService.addRoleToUser(
                userAndRoleBuilder.getUsername(),
                userAndRoleBuilder.getRoleName()
        );
    }

    @DeleteMapping("/users/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username) {
        userAccountService.deleteUserAccount(username);
        return ResponseEntity.ok(Map.of(
                "message", "User '" + username + "' deleted successfully"
        ));
    }

    @DeleteMapping("/roles/{roleName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable String roleName) {
        userAccountService.deleteRole(roleName);
        return ResponseEntity.ok(Map.of(
                "message", "Role '" + roleName + "' deleted successfully"
        ));
    }

    @DeleteMapping("/users/{username}/roles/{roleName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> removeRoleFromUser(
            @PathVariable String username,
            @PathVariable String roleName) {
        userAccountService.removeRoleFromUser(username, roleName);
        return ResponseEntity.ok(Map.of(
                "message", "Role '" + roleName + "' removed from user '" + username + "' successfully"
        ));
    }


    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public AppUserDTO profile(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return userAccountService.loadUserByUsername(username);
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String authorizationHeader = request.getHeader(JwtUtil.HEADER_STRING);

        if (authorizationHeader == null || !authorizationHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
            throw new InvalidRefreshTokenException("Refresh token is missing or invalid");
        }

        try {
            String refreshToken = authorizationHeader.substring(JwtUtil.TOKEN_PREFIX.length());

            var secretKey = Keys.hmacShaKeyFor(JwtUtil.SECRET.getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            String username = claims.getSubject();


            var appUser = userAccountServiceImpl.loadUserEntityByUsername(username);

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

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access-token", jwtAccessToken);
            tokens.put("refresh-token", refreshToken);

            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException("Refresh token has expired. Please login again.");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new InvalidRefreshTokenException("Invalid refresh token signature");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new InvalidRefreshTokenException("Malformed refresh token");
        } catch (ResourceNotFoundException e) {
            throw new InvalidRefreshTokenException("User associated with token not found");
        } catch (Exception e) {
            throw new InvalidRefreshTokenException("Invalid refresh token: " + e.getMessage());
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