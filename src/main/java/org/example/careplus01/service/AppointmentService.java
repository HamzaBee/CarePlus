package org.example.careplus01.service;

import org.example.careplus01.DTO.AppointmentDTO;
import org.example.careplus01.enums.StatusAPT;
import org.example.careplus01.enums.TypeAppointment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO getAppointmentById(Long id);
    AppointmentDTO updateStatus(Long id, StatusAPT newStatus);
    void deleteAppointment(Long id);
    List<AppointmentDTO> getAppointmentsByDate(LocalDate date);
    List<AppointmentDTO> getAppointmentsByPatientId(UUID patientId);
    List<AppointmentDTO> getAppointmentsByStatus(StatusAPT status);
    List<AppointmentDTO> getAppointmentsByType(TypeAppointment typeAppointment);
}
