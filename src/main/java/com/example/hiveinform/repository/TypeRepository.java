package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TypeRepository extends JpaRepository<Type,Long> {
    Type getTypesByUserId(long id);
}
