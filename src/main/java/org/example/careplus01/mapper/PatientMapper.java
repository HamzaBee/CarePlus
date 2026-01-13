package org.example.careplus01.mapper;
import org.example.careplus01.DTO.PatientDTO;
import org.example.careplus01.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PatientMapper {
    PatientDTO toDTO(Patient patient);
    Patient toEntity(PatientDTO dto);
    List<PatientDTO> toDTOList(List<Patient> patients);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "insuranceNumber", ignore = true)
    void updatePatientFromDTO(PatientDTO patientDTO, @MappingTarget Patient patient);
}
