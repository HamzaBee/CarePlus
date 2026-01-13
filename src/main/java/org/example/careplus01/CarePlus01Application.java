package org.example.careplus01;

import org.example.careplus01.entity.Patient;
import org.example.careplus01.enums.Gender;
import org.example.careplus01.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class CarePlus01Application {

    public static void main(String[] args) {
        SpringApplication.run(CarePlus01Application.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository) {
        return args -> {

            Patient p1 = new Patient();
            p1.setFirstName("Hamza");
            p1.setLastName("Benbrahim");
            p1.setGender(Gender.MALE);
            p1.setDateOfBirth(LocalDate.of(1998, 5, 20));
            p1.setActive(true);
            p1.setInsuranceNumber("123456789");
            p1.setPhoneNumber("123456789");
            p1.setAddress("Kasbat Amine");
            p1.setCity("Casablanca");
            p1.setCountry("Maroc");
            p1.setState("Casa,Settat");
            patientRepository.save(p1);

            System.out.println("Patient de test ajouté !");
        };
    }

}
