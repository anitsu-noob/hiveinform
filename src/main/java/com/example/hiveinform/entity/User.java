package com.example.hiveinform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.netty.util.concurrent.ThreadPerTaskExecutor;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

@lombok.Data
@Table
@Entity
@RequiredArgsConstructor
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id ;
    @Column
    private String username ;
    @Column
    private String password ;
    @DateTimeFormat(iso = ISO.DATE,fallbackPatterns = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date birthDate ;
    private String fullName ;
    private String address ;
    private String email ;
    private String type ;
    private String role ;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<SimpleGrantedAuthority>();
        authorityList.add(new SimpleGrantedAuthority(role));
        return authorityList;
    }
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
