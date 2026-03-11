package org.example.careplus01.serviceImpl;

import lombok.AllArgsConstructor;
import org.example.careplus01.DTO.AppRoleDTO;
import org.example.careplus01.DTO.AppUserCreateDTO;
import org.example.careplus01.DTO.AppUserDTO;
import org.example.careplus01.DTO.AppUserUpdateDTO;
import org.example.careplus01.entity.AppRole;
import org.example.careplus01.entity.AppUser;
import org.example.careplus01.exception.BadRequestException;
import org.example.careplus01.exception.ResourceAlreadyExistsException;
import org.example.careplus01.exception.ResourceNotFoundException;
import org.example.careplus01.mapper.AppRoleMapper;
import org.example.careplus01.mapper.AppUserMapper;
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
    private final AppUserMapper appUserMapper;
    private final AppRoleMapper appRoleMapper;

    @Override
    public AppUserDTO addNewUserAccount(AppUserCreateDTO createDTO) {

        if (appUserRepository.findByUsername(createDTO.getUsername()) != null) {
            throw new ResourceAlreadyExistsException(
                    "User with username '" + createDTO.getUsername() + "' already exists"
            );
        }

        if (createDTO.getEmail() != null &&
                appUserRepository.findByEmail(createDTO.getEmail()) != null) {
            throw new ResourceAlreadyExistsException(
                    "User with email '" + createDTO.getEmail() + "' already exists"
            );
        }

        // Map DTO to Entity
        AppUser appUser = appUserMapper.toEntity(createDTO);

        // Encode password
        appUser.setHashPassword(passwordEncoder.encode(createDTO.getPassword()));

        // Save and return DTO
        AppUser savedUser = appUserRepository.save(appUser);
        return appUserMapper.toDTO(savedUser);
    }

    @Override
    public AppUserDTO updateUserAccount(String username, AppUserUpdateDTO updateDTO) {

        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // Check if email is being changed and if it already exists
        if (updateDTO.getEmail() != null &&
                !updateDTO.getEmail().equals(appUser.getEmail())) {
            AppUser existingUser = appUserRepository.findByEmail(updateDTO.getEmail());
            if (existingUser != null) {
                throw new ResourceAlreadyExistsException(
                        "User with email '" + updateDTO.getEmail() + "' already exists"
                );
            }
        }

        // Update entity from DTO
        appUserMapper.updateEntityFromDTO(updateDTO, appUser);

        // Save and return DTO
        AppUser updatedUser = appUserRepository.save(appUser);
        return appUserMapper.toDTO(updatedUser);
    }

    @Override
    public AppRoleDTO addNewRole(AppRoleDTO roleDTO) {

        if (appRoleRepository.findByRoleName(roleDTO.getRoleName()) != null) {
            throw new ResourceAlreadyExistsException(
                    "Role '" + roleDTO.getRoleName() + "' already exists"
            );
        }

        if (roleDTO.getRoleName() == null || roleDTO.getRoleName().trim().isEmpty()) {
            throw new BadRequestException("Role name cannot be empty");
        }

        // Map DTO to Entity, save, and map back to DTO
        AppRole appRole = appRoleMapper.toEntity(roleDTO);
        AppRole savedRole = appRoleRepository.save(appRole);
        return appRoleMapper.toDTO(savedRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }

        if (roleName == null || roleName.trim().isEmpty()) {
            throw new BadRequestException("Role name cannot be empty");
        }

        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appRole == null) {
            throw new ResourceNotFoundException("Role not found: " + roleName);
        }

        if (appUser.getRoles().contains(appRole)) {
            throw new ResourceAlreadyExistsException(
                    "User '" + username + "' already has role '" + roleName + "'"
            );
        }

        appUser.getRoles().add(appRole);
        appUserRepository.save(appUser);
    }

    @Override
    public AppUserDTO loadUserByUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }

        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        return appUserMapper.toDTO(appUser);
    }

    @Override
    public List<AppUserDTO> listUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return appUserMapper.toDTOList(users);
    }

    @Override
    public List<AppRoleDTO> listRoles() {
        List<AppRole> roles = appRoleRepository.findAll();
        return appRoleMapper.toDTOList(roles);
    }

    // Helper method for UserDetailsService (returns entity)
    public AppUser loadUserEntityByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }

        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        return appUser;
    }

    @Override
    public void deleteUserAccount(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }

        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // Clear roles association before deleting
        appUser.getRoles().clear();
        appUserRepository.save(appUser);

        appUserRepository.delete(appUser);
    }

    @Override
    public void deleteRole(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new BadRequestException("Role name cannot be empty");
        }

        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appRole == null) {
            throw new ResourceNotFoundException("Role not found: " + roleName);
        }

        // Check if any users have this role
        List<AppUser> usersWithRole = appUserRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(appRole))
                .toList();

        if (!usersWithRole.isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete role '" + roleName + "'. It is assigned to " +
                            usersWithRole.size() + " user(s). Remove the role from all users first."
            );
        }

        appRoleRepository.delete(appRole);
    }

    @Override
    public void removeRoleFromUser(String username, String roleName) {
        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Username cannot be empty");
        }

        if (roleName == null || roleName.trim().isEmpty()) {
            throw new BadRequestException("Role name cannot be empty");
        }

        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appRole == null) {
            throw new ResourceNotFoundException("Role not found: " + roleName);
        }

        if (!appUser.getRoles().contains(appRole)) {
            throw new ResourceNotFoundException(
                    "User '" + username + "' does not have role '" + roleName + "'"
            );
        }

        appUser.getRoles().remove(appRole);
        appUserRepository.save(appUser);
    }


}