package com.dscatalog.dto;

import com.dscatalog.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotBlank(message = "Campo obrigatório")
    private String firstName;

    private String lastName;

    @Email(message = "Favor entrar com um e-mail válido")
    private String email;

    Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

}
