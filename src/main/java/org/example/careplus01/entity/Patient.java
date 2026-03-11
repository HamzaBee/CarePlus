package org.example.careplus01.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.careplus01.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;
    @Column(length = 20)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;
    private String country;
    private String city;
    private String address;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    private String state;
    @Column(unique = true)
    private String insuranceNumber;
    @Column(nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL,orphanRemoval = true)
    @OrderBy("date DESC")
    private List<Appointment> appointments;

}
