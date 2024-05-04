package com.example.hiveinform.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 2277839177L;

    private String username;
    private String password;

}
