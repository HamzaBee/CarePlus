package org.example.careplus01.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.careplus01.DTO.AppointmentDTO;
import org.example.careplus01.enums.StatusAPT;
import org.example.careplus01.enums.TypeAppointment;
import org.example.careplus01.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody @Valid AppointmentDTO dto) {
        AppointmentDTO savedAppointment = appointmentService.createAppointment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
    }
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO>  updateAppointment(@PathVariable long id, @RequestBody @Valid AppointmentDTO dto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id,dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateStatus(@PathVariable long id, @RequestBody StatusAPT newStatus) {
        return ResponseEntity.ok(appointmentService.updateStatus(id,newStatus));
    }
    @GetMapping("/search/date")
    public ResponseEntity<List<AppointmentDTO>> searchAppointmentsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return  ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
    }
    @GetMapping("/search/patient")
    public ResponseEntity<List<AppointmentDTO>> searchAppointmentsByPatient(@RequestParam UUID patientId) {
      return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }
    @GetMapping("/search/status")
    public ResponseEntity<List<AppointmentDTO>> searchAppointmentsByStatus(@RequestParam StatusAPT status) {
        return  ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }
    @GetMapping("/search/type")
    public ResponseEntity<List<AppointmentDTO>>getAppointmentsByType(@RequestParam TypeAppointment typeAppointment) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByType(typeAppointment));
    }
}
