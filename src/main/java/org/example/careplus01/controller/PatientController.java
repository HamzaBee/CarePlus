package org.example.careplus01.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.careplus01.DTO.PatientDTO;
import org.example.careplus01.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

@PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody @Valid PatientDTO patientDTO) {
    PatientDTO savedPatient = patientService.create(patientDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);

}
@PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable UUID id, @RequestBody @Valid PatientDTO patientDTO) {
    return  ResponseEntity.ok(patientService.update(id,patientDTO));
}
@GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable UUID id) {
    return ResponseEntity.ok(patientService.getPatientById(id));
}
@GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> searchPatients(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return ResponseEntity.ok(patientService.searchPatient(keyword));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }



}
