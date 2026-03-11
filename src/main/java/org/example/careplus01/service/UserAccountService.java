package org.example.careplus01.service;

import org.example.careplus01.DTO.AppRoleDTO;
import org.example.careplus01.DTO.AppUserCreateDTO;
import org.example.careplus01.DTO.AppUserDTO;
import org.example.careplus01.DTO.AppUserUpdateDTO;

import java.util.List;

public interface UserAccountService {
    AppUserDTO addNewUserAccount(AppUserCreateDTO createDTO);
    AppUserDTO updateUserAccount(String username, AppUserUpdateDTO updateDTO);
    AppRoleDTO addNewRole(AppRoleDTO roleDTO);
    void addRoleToUser(String username, String roleName);
    AppUserDTO loadUserByUsername(String username);
    List<AppUserDTO> listUsers();
    List<AppRoleDTO> listRoles();
    void deleteUserAccount(String username);
    void deleteRole(String roleName);
    void removeRoleFromUser(String username, String roleName);


}
