package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.TestingDto;
import com.example.hiveinform.entity.Testing;
import com.example.hiveinform.repository.TestingRepository;
import com.example.hiveinform.service.TestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("testingService")
public class TestingServiceImpl implements TestingService {
    @Autowired
    private TestingRepository testingRepository;
    @Override
    public Testing addQuestion(TestingDto testingDto) {
        Testing question = new Testing();
        question.setType(testingDto.getType());
        question.setAttribute(testingDto.getAttribute());
        question.setTitle(testingDto.getTitle());
        question.setContent(testingDto.getContent());
        question.setAnswer(testingDto.getAnswer());
        return testingRepository.save(question);
    }

    @Override
    public void deleteQuestion(Long id) {
        testingRepository.deleteById(id);
    }

    @Override
    public List<Testing> getQuestionByType(String type) {
       List<Testing> testings = testingRepository.getTestingByType(type);
        if (testings == null) {
            throw  new IllegalStateException("非法注入");
        }
        List<Testing> questions = new ArrayList<>();
        Random random = new Random();
        if(testings.size()<=25)
            return testings;
        for (int i = 0; i < 25; i++)
        {
            int test_num = testings.size()-1;
            int j = random.nextInt(test_num);
            questions.add(testings.get(j));
            testings.remove(j);
        }
        return questions;
    }
    @Override
    public Testing updateQuestion(Testing testing) {
        Testing question = testingRepository.findById(testing.getId()).orElse(null);
        if (question == null) {
            return new Testing();
        }
        return testingRepository.save(testing);
    }
    @Override
    public List<Testing> getAllQuestions() {
        return testingRepository.findAll();
    }
}
