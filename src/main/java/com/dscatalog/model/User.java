package com.dscatalog.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long id;
    private String firstName;
    private String LastName;
    private String email;
    private String password;

}
