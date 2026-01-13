package org.example.careplus01;

import org.example.careplus01.entity.AppRole;
import org.example.careplus01.entity.AppUser;
import org.example.careplus01.service.UserAccountService;
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