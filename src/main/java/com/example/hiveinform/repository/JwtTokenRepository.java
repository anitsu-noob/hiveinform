package com.example.hiveinform.repository;

import com.example.hiveinform.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface JwtTokenRepository extends CrudRepository<JwtToken, String> {
        JwtToken findByToken(String token);

         void deleteByToken(String token);
}
