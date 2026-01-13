package org.example.careplus01;

import org.example.careplus01.entity.AppRole;
import org.example.careplus01.entity.AppUser;
import org.example.careplus01.service.UserAccountService;
import org.springframework.boot.CommandLineRunner;
import org.example.careplus01.entity.Patient;
import org.example.careplus01.enums.Gender;
import org.example.careplus01.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;

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



    @Bean
    CommandLineRunner start(UserAccountService userAccountService) {
        return args -> {


            userAccountService.addNewRole(new AppRole(null, "ROLE_USER"));
            userAccountService.addNewRole(new AppRole(null, "ROLE_ADMIN"));
            userAccountService.addNewRole(new AppRole(null, "ROLE_DOCTOR"));
            userAccountService.addNewRole(new AppRole(null, "ROLE_SECRETARY"));


            userAccountService.addNewUserAccount(new AppUser(
                    null,
                    "admin01",
                    "mehdi",
                    "hyndi",
                    "1234",
                    "admin01@careplus.com",
                    "0612154654",
                    LocalDate.now(),
                    new ArrayList<>()
            ));

            userAccountService.addNewUserAccount(new AppUser(
                    null,
                    "secretary01",
                    "sofia",
                    "amerani",
                    "1234",
                    "secretary01@careplus.com",
                    "0612497865",
                    LocalDate.now(),
                    new ArrayList<>()
            ));

            userAccountService.addNewUserAccount(new AppUser(
                    null,
                    "doctor01",
                    "younes",
                    "ouakrim",
                    "1234",
                    "doctor01@careplus.com",
                    "0648489781",
                    LocalDate.now(),
                    new ArrayList<>()
            ));


            userAccountService.addRoleToUser("admin01", "ROLE_USER");
            userAccountService.addRoleToUser("admin01", "ROLE_ADMIN");

            userAccountService.addRoleToUser("secretary01", "ROLE_USER");
            userAccountService.addRoleToUser("secretary01", "ROLE_SECRETARY");

            userAccountService.addRoleToUser("doctor01", "ROLE_USER");
            userAccountService.addRoleToUser("doctor01", "ROLE_DOCTOR");


        };
    }
}