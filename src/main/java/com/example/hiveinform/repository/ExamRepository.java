package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ExamRepository extends JpaRepository<Exam, Long> {
}
