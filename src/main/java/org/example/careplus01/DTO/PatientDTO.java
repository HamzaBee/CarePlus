package org.example.careplus01.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.careplus01.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PatientDTO {

    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Size(min = 2, max = 255)
    private String address;

    private String country;
    private String city;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String state;

    @NotBlank(message = "Insurance number is required")
    private String insuranceNumber;

}
