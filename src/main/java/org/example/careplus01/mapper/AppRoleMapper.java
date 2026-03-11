package org.example.careplus01.mapper;

import org.example.careplus01.DTO.AppRoleDTO;
import org.example.careplus01.entity.AppRole;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppRoleMapper {

    AppRoleDTO toDTO(AppRole appRole);

    AppRole toEntity(AppRoleDTO appRoleDTO);

    List<AppRoleDTO> toDTOList(List<AppRole> appRoles);
}