package org.example.careplus01.service;
import org.example.careplus01.DTO.PatientDTO;
import org.example.careplus01.entity.Patient;
import java.util.List;
import java.util.UUID;

public interface PatientService {
    PatientDTO create(PatientDTO patientDTO);
    PatientDTO update(UUID id, PatientDTO patientDTO);
    PatientDTO getPatientById(UUID id);
    List<PatientDTO> getAllPatients();
    List<PatientDTO>searchPatient(String keyword);
    void deletePatientById(UUID id);



}
