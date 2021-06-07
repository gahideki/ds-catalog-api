package com.dscatalog.dto;

import com.dscatalog.service.validation.UserUpdateValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UserUpdateValid
public class UserUpdateDTO extends UserDTO {

}
