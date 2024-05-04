package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Exam;
import com.example.hiveinform.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {


    ExamResult getExamResultByUserId(Long userId);
    ExamResult getExamResultByExamId(Long examId);
}