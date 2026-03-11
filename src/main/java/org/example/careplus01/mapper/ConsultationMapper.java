package org.example.careplus01.mapper;

import org.example.careplus01.DTO.ConsultationDTO;
import org.example.careplus01.entity.Consultation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ConsultationMapper {
    @Mapping(source = "appointment.id", target = "appointmentId")
    ConsultationDTO toDTO(Consultation consultation);

    List<ConsultationDTO> toDTOList(List<Consultation> consultation);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    Consultation toEntity(ConsultationDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    void updateEntity(ConsultationDTO dto, @MappingTarget Consultation consultation);

}

