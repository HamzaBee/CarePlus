package org.example.careplus01.mapper;

import org.example.careplus01.DTO.AppUserCreateDTO;
import org.example.careplus01.DTO.AppUserDTO;
import org.example.careplus01.DTO.AppUserUpdateDTO;
import org.example.careplus01.entity.AppUser;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = AppRoleMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AppUserMapper {


    AppUserDTO toDTO(AppUser appUser);

    List<AppUserDTO> toDTOList(List<AppUser> appUsers);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashPassword", source = "password")
    @Mapping(target = "createDate", expression = "java(getCurrentDate())")
    @Mapping(target = "roles", ignore = true)
    AppUser toEntity(AppUserCreateDTO createDTO);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "hashPassword", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromDTO(AppUserUpdateDTO updateDTO, @MappingTarget AppUser appUser);


    default LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}