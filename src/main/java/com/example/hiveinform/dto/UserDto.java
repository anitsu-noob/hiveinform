package com.example.hiveinform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
@Data
public class UserDto {
    private long id ;
    private String username ;
    private String password ;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,fallbackPatterns = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date birthDate ;
    private String fullName ;
    private String address ;
    private String email ;
    private String type ;
    private String role ;
}
