package org.example.careplus01.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.careplus01.enums.StatusAPT;
import org.example.careplus01.enums.TypeAppointment;

import java.time.LocalDateTime;



@Entity
@Data @AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime date;
    private TypeAppointment typeAppointment;
    @Enumerated(EnumType.STRING)
    private StatusAPT status;
    @ManyToOne
        @JoinColumn(name = "patient_id", nullable = false)
        private Patient patient;
}

