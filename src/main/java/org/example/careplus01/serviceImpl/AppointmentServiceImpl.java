package org.example.careplus01.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.careplus01.DTO.AppointmentDTO;
import org.example.careplus01.entity.Appointment;
import org.example.careplus01.entity.Patient;
import org.example.careplus01.enums.StatusAPT;
import org.example.careplus01.enums.TypeAppointment;
import org.example.careplus01.exception.ResourceNotFoundException;
import org.example.careplus01.mapper.AppointmentMapper;
import org.example.careplus01.repository.AppointmentRepository;
import org.example.careplus01.repository.PatientRepository;
import org.example.careplus01.service.AppointmentService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO dto){
        Appointment appointment = appointmentMapper.toEntity(dto);

        Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow(()->new IllegalArgumentException("Patient not found"));
        appointment.setPatient(patient);
        if(appointment.getStatus() == null){
            appointment.setStatus(StatusAPT.PLANNED);
        }

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentMapper.toDTOList(appointmentRepository.findAll());
    }

    @Override
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Appointment not found"));
        return appointmentMapper.toDTO(appointment);
    }

    @Override
    public AppointmentDTO updateStatus(Long id, StatusAPT newStatus){
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Appointment not found"));
        appointment.setStatus(newStatus);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        if(!appointmentRepository.existsById(id)){
            throw new ResourceNotFoundException("Appointment not found");
        }
     appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByDate(LocalDate date) {
        // Transformation de "16/01/2026" en "16/01/2026 00:00:00"
        LocalDateTime start = date.atStartOfDay();
        // Transformation de "16/01/2026" en "16/01/2026 23:59:59"
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<Appointment> list = appointmentRepository.findByDateBetween(start, end);
        return appointmentMapper.toDTOList(list);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByPatientId(UUID patientId) {
        if(!patientRepository.existsById(patientId)){
            throw new ResourceNotFoundException("Patient not found");
        }

        return appointmentMapper.toDTOList(appointmentRepository.findByPatientId(patientId));
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByStatus(StatusAPT status) {
        return appointmentMapper.toDTOList(appointmentRepository.findByStatus(status));
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByType(TypeAppointment typeAppointment) {
        return appointmentMapper.toDTOList(appointmentRepository.findByTypeAppointment(typeAppointment));
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Appointment not found"));
        appointmentMapper.updateAppointmentFromDTO(dto, appointment);
        if (dto.getPatientId() != null && !dto.getPatientId().equals(appointment.getPatient().getId())) {
            Patient newPatient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
            appointment.setPatient(newPatient);
        }
        Appointment updateAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(updateAppointment);
    }
}
