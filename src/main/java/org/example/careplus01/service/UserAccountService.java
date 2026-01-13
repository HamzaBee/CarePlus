package org.example.careplus01.service;

import org.example.careplus01.entity.AppRole;
import org.example.careplus01.entity.AppUser;

import java.util.List;

public interface UserAccountService {
    AppUser addNewUserAccount(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
    List<AppRole> listRoles();
}
