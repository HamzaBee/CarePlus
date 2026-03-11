package org.example.careplus01.DTO;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//request for updating users
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AppUserUpdateDTO {

    private String firstName;
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;
}
