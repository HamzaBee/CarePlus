package org.example.careplus01.repository;


import org.example.careplus01.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface PatientRepository extends JpaRepository<Patient, UUID>{
    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    boolean existsByInsuranceNumber(String insuranceNumber);
}
