package org.example.careplus01.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//response dto
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppRoleDTO {
    private Long id;
    private String roleName;
}
