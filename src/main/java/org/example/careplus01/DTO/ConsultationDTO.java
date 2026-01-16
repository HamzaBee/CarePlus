package org.example.careplus01.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDTO {
    private UUID id;
    @NotBlank(message = "Report is required")
    private String report;
    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;
    @NotNull(message = "Start time is required")
    private LocalDateTime actualStartTime;
    private String treatment;
    @NotNull(message = "Fee is required")
    @DecimalMin(value = "0.0", message = "Fee must be positive")
    @Digits(integer = 8, fraction = 4)
    private BigDecimal fee;
    private boolean paid;
    @NotNull(message = "Appointment ID is required")
    private UUID appointmentId;



}
