package org.example.careplus01.repository;

import org.example.careplus01.entity.Appointment;
import org.example.careplus01.enums.StatusAPT;
import org.example.careplus01.enums.TypeAppointment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(UUID patientId);
    List<Appointment> findByStatus(StatusAPT status);
    List<Appointment> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Appointment> findByTypeAppointment(TypeAppointment typeAppointment);
}
