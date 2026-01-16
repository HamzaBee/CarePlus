package org.example.careplus01.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consultation")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(columnDefinition = "TEXT")
    private String report;

    private String diagnosis;
    private LocalDateTime actualStartTime;
    private String treatment;
    private BigDecimal fee;
    @Column(nullable = false)
    private boolean paid = false;
    @OneToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id", nullable = false, unique = true)
    private Appointment appointment;
}
