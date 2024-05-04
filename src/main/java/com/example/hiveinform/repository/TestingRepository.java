package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Testing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TestingRepository extends JpaRepository<Testing, Long> {
    List<Testing> getTestingByType(String type);
}
