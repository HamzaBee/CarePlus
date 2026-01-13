package org.example.careplus01.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.careplus01.DTO.PatientDTO;
import org.example.careplus01.entity.Patient;
import org.example.careplus01.exception.ResourceAlreadyExistsException;
import org.example.careplus01.exception.ResourceNotFoundException;
import org.example.careplus01.mapper.PatientMapper;
import org.example.careplus01.repository.PatientRepository;
import org.example.careplus01.service.PatientService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;


    @Override
    public PatientDTO create(PatientDTO patientDTO) {
        if(patientRepository.existsByInsuranceNumber(patientDTO.getInsuranceNumber())) {
            throw new ResourceAlreadyExistsException("Insurance number already exists");
        }
        Patient patient = patientMapper.toEntity(patientDTO);
        Patient savePatient = patientRepository.save(patient);

       return patientMapper.toDTO(savePatient);
    }

    @Override
    public PatientDTO update(UUID id, PatientDTO patientDTO) {
        Patient existingPatient = patientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Patient not found"));
        if (patientDTO.getInsuranceNumber() != null
                && !patientDTO.getInsuranceNumber().equals(existingPatient.getInsuranceNumber())) {
            if (patientRepository.existsByInsuranceNumber(patientDTO.getInsuranceNumber())) {
                throw new ResourceAlreadyExistsException("Insurance number already exists");
            }
            existingPatient.setInsuranceNumber(patientDTO.getInsuranceNumber());
            }
        patientMapper.updatePatientFromDTO(patientDTO, existingPatient);

        return patientMapper.toDTO(patientRepository.save(existingPatient));
            }

    @Override
    public PatientDTO getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Patient not found with id: " + id));

        return patientMapper.toDTO(patient);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientMapper.toDTOList(patientRepository.findAll());
    }

    @Override
    public List<PatientDTO> searchPatient(String keyword) {
        List<Patient> patients = patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);
        return patientMapper.toDTOList(patients);
    }

    @Override
    public void deletePatientById(UUID id) {
      if(!patientRepository.existsById(id)) {
          throw new ResourceNotFoundException("Patient not found with id: " + id);
      }
      patientRepository.deleteById(id);
    }
}
