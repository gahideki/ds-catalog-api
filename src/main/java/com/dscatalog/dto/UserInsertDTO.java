package com.dscatalog.dto;

import com.dscatalog.service.validation.UserInsertValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UserInsertValid
public class UserInsertDTO extends UserDTO {

    private String password;

}
