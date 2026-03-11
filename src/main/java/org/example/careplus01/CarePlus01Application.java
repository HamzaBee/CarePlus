package org.example.careplus01;

import org.example.careplus01.DTO.AppRoleDTO;
import org.example.careplus01.DTO.AppUserCreateDTO;
import org.example.careplus01.service.UserAccountService;
import org.springframework.boot.CommandLineRunner;
import org.example.careplus01.entity.Patient;
import org.example.careplus01.enums.Gender;
import org.example.careplus01.repository.PatientRepository;
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
    CommandLineRunner commandLineRunner(PatientRepository patientRepository , UserAccountService userAccountService) {
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


            // creating roles
            userAccountService.addNewRole(AppRoleDTO.builder()
                    .roleName("ROLE_USER")
                    .build());
            userAccountService.addNewRole(AppRoleDTO.builder()
                    .roleName("ROLE_ADMIN")
                    .build());
            userAccountService.addNewRole(AppRoleDTO.builder()
                    .roleName("ROLE_DOCTOR")
                    .build());
            userAccountService.addNewRole(AppRoleDTO.builder()
                    .roleName("ROLE_SECRETARY")
                    .build());


            // creating user accounts
            userAccountService.addNewUserAccount(AppUserCreateDTO.builder()
                    .username("admin01")
                    .firstName("mehdi")
                    .lastName("hyndi")
                    .password("1234")
                    .email("admin01@careplus.com")
                    .phoneNumber("0612154654")
                    .build());

            userAccountService.addNewUserAccount(AppUserCreateDTO.builder()
                    .username("secretary01")
                    .firstName("sofia")
                    .lastName("amerani")
                    .password("1234")
                    .email("secretary01@careplus.com")
                    .phoneNumber("0612497865")
                    .build());

            userAccountService.addNewUserAccount(AppUserCreateDTO.builder()
                    .username("doctor01")
                    .firstName("younes")
                    .lastName("ouakrim")
                    .password("1234")
                    .email("doctor01@careplus.com")
                    .phoneNumber("0648489781")
                    .build());

            //assigning roles to users
            userAccountService.addRoleToUser("admin01", "ROLE_USER");
            userAccountService.addRoleToUser("admin01", "ROLE_ADMIN");

            userAccountService.addRoleToUser("secretary01", "ROLE_USER");
            userAccountService.addRoleToUser("secretary01", "ROLE_SECRETARY");

            userAccountService.addRoleToUser("doctor01", "ROLE_USER");
            userAccountService.addRoleToUser("doctor01", "ROLE_DOCTOR");

        };
    }

    }
