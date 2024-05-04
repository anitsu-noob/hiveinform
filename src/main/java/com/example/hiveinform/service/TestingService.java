package com.example.hiveinform.service;

import com.example.hiveinform.dto.TestingDto;
import com.example.hiveinform.entity.Testing;

import java.util.List;

public interface TestingService {
    Testing addQuestion(TestingDto testingDto);
    Testing updateQuestion(Testing testing);
    List<Testing> getAllQuestions();
    void deleteQuestion(Long id);
    List<Testing> getQuestionByType(String type);
}
