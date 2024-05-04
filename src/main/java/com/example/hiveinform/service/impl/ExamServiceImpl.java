package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.ExamDto;
import com.example.hiveinform.dto.ExamResultDto;
import com.example.hiveinform.entity.*;
import com.example.hiveinform.repository.ExamRepository;
import com.example.hiveinform.repository.ExamResultRepository;
import com.example.hiveinform.repository.PaperRepository;
import com.example.hiveinform.repository.UserRepository;
import com.example.hiveinform.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service("examService")
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private ExamResultRepository examResultRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Exam createExam(@RequestBody ExamDto examDto) {
       Paper paper = examDto.getPaper();
        if (paper == null) {
            return new Exam();
        }
        Exam exam = new Exam();
        exam.setName(examDto.getName());
        exam.setPaper(paper);
        Exam save = examRepository.save(exam);
        paper.setExam_id(save.getId());
        paperRepository.save(paper);
        return save;
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    public ExamResult submitExam(Long examId, Long userId, List<String> answers) {
        Exam exam = examRepository.findById(examId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (exam == null || user == null) {
            throw new RuntimeException("非法注入");
        }
        int score = 0;
        List<Testing> questions = exam.getPaper().getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Testing question = questions.get(i);
            if (question.getAnswer().equals(answers.get(i).toUpperCase())) {
                score++;                                            // 判定分数 答案形式 以大写上传 认准格式
            }
        }
        ExamResult result = new ExamResult();
        result.setExam(exam);
        result.setUser(user);
        result.setScore(score);
        return examResultRepository.save(result);
    }

    @Override
    public ExamResult getExamResults(Long examId) {
        Exam exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            return new ExamResult();
        }
        return exam.getResult();
    }

    @Override
    public Exam deleteExam(Long examId) {
        ExamResult examResult = examResultRepository.getExamResultByExamId(examId);
//        if (examResult == null) {
//            return null;
//        }
        Exam exam = examRepository.findById(examId).orElse(null);

        if (exam == null) {
            throw new IllegalArgumentException("非法注入");
        }

        examRepository.delete(exam);  // cascade 会在删除exam后一起删除 paper
        return exam;
    }

}
