package com.example.hiveinform.service;

import com.example.hiveinform.dto.ExamDto;
import com.example.hiveinform.dto.ExamResultDto;
import com.example.hiveinform.entity.Exam;
import com.example.hiveinform.entity.ExamResult;
import com.example.hiveinform.entity.Paper;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamService {
    Exam createExam(ExamDto examDto);
    List<Exam> getAllExams();
    ExamResult submitExam(Long examId, Long userId, List<String> answers);
    ExamResult getExamResults(Long examId);
    Exam deleteExam(Long examId);
}
