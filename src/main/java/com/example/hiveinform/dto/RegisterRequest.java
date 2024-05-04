package com.example.hiveinform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class RegisterRequest {
    @NotNull
    @Size(min=5,max = 18)
    private String username ;
    @NotNull
    @Size(min=8,max = 16)
    private String password ;
    @Past
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date birthDate ;
    @NotBlank
    private String fullName ;
    private String address ;
    private String email ;
    private String code ;
    private String type ;
}
