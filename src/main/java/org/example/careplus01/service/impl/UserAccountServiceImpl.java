package org.example.careplus01.service.impl;

import lombok.AllArgsConstructor;
import org.example.careplus01.entity.AppRole;
import org.example.careplus01.entity.AppUser;
import org.example.careplus01.repository.AppRoleRepository;
import org.example.careplus01.repository.AppUserRepository;
import org.example.careplus01.service.UserAccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUserAccount(AppUser appUser) {
        String password = appUser.getHashPassword();
        appUser.setHashPassword(passwordEncoder.encode(password));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);

        if (appUser != null && appRole != null) {
            appUser.getRoles().add(appRole);
            appUserRepository.save(appUser);
        } else {
            throw new RuntimeException("User or Role not found");
        }
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public List<AppRole> listRoles() {
        return appRoleRepository.findAll();
    }
}