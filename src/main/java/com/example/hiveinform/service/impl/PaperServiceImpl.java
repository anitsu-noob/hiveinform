package com.example.hiveinform.service.impl;

import com.example.hiveinform.entity.*;
import com.example.hiveinform.repository.*;
import com.example.hiveinform.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("paperService")
public class PaperServiceImpl implements PaperService {

    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private ExamRepository examRepository ;
    @Autowired
    private ExamResultRepository examResultRepository;
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private TestingRepository testingRepository;
    @Override
    public Paper addPaper(String name, List<Long> questionIds) {
        User user = userRepository.getUserByUsername(name);
        if (user == null) { throw new RuntimeException("非法注入") ;}
//        Exam exam = examResultRepository.getExamResultByUserId(user.getId()).getExam();
        Paper paper = new Paper();
//        paper.setExam(exam);   // 由exam 创建
        paper.setName(name);
        List<Testing> questions = testingRepository.findAllById(questionIds);
        paper.setQuestions(questions);
        return paperRepository.save(paper);
    }
    @Override
    public void deletePaper(Long id) {
        paperRepository.deleteById(id);
    }
    @Override
    @Transactional
    public Paper updatePaper(Long id, String name, List<Long> questionIds) {
        Paper paper = paperRepository.findById(id).orElse(null);
        if (paper == null) {
            return new Paper();
        }
        paper.setName(name);
        List<Testing> questions = testingRepository.findAllById(questionIds);
        paper.setQuestions(questions);
        return paperRepository.save(paper);
    }
    @Override
    public List<Paper> getAllPapers() {
        return paperRepository.findAll();
    }
}
