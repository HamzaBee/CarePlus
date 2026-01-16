package org.example.careplus01.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.careplus01.enums.StatusAPT;
import org.example.careplus01.enums.TypeAppointment;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AppointmentDTO {

    private Long id;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime date;

    @NotNull(message = "Appointment type is required")
    private TypeAppointment typeAppointment;
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private StatusAPT status;

    private String patientFirstName;
    private String patientLastName;
    private String patientPhone;

}
