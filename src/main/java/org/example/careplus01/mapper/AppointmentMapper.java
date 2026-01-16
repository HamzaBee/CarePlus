package org.example.careplus01.mapper;

import org.example.careplus01.DTO.AppointmentDTO;
import org.example.careplus01.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.firstName", target = "patientFirstName")
    @Mapping(source = "patient.lastName", target = "patientLastName")
    @Mapping(source = "patient.phoneNumber", target = "patientPhone")
    AppointmentDTO toDTO(Appointment appointment);

    List<AppointmentDTO> toDTOList(List<Appointment> appointments);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "id", ignore = true)
    Appointment toEntity(AppointmentDTO dto);
    @Mapping(target = "id", ignore = true)      // On ne change jamais l'ID d'une entité existante
    @Mapping(target = "patient", ignore = true) // On gère la relation manuellement dans le service (plus sûr)
    void updateAppointmentFromDTO(AppointmentDTO dto, @MappingTarget Appointment appointment);

}
