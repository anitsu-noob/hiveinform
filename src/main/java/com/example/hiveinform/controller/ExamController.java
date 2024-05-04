package com.example.hiveinform.controller;

import com.example.hiveinform.dto.ExamDto;
import com.example.hiveinform.dto.ExamResultDto;
import com.example.hiveinform.entity.Exam;
import com.example.hiveinform.entity.ExamResult;
import com.example.hiveinform.entity.Paper;
import com.example.hiveinform.entity.Testing;
import com.example.hiveinform.service.ExamService;
import com.example.hiveinform.service.PaperService;
import com.example.hiveinform.service.TestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private TestingService testingService;
    @Autowired
    private ExamService examService;

    @Autowired
    private PaperService paperService;

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/create")   // 创建考试
    @Transactional
    public Exam createExam(@RequestBody ExamDto examDto) {
        List<Testing> questions = testingService.getQuestionByType(examDto.getName());
        List<Long> questionsId = new ArrayList<>();
        for (Testing question : questions) {
            questionsId.add(question.getId());
        }   //获取题目
        Paper paper = paperService.addPaper(examDto.getPaper().getName(), questionsId); // 创建 试卷
        examDto.setPaper(paper);  // 获取试卷的id
//        Exam exam = new Exam();
//        exam.setName(examDto.getName());
//        exam.setPaper(paper);

        return examService.createExam(examDto);  // 创建考试
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("/all")
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/submit")
    public ExamResult submitExam(@RequestBody ExamResultDto examResultDto) {
        return examService.submitExam(examResultDto.getExamID(), examResultDto.getUserID(), examResultDto.getAnswer());   // 提交考试和成绩  需要重新作答
    } // 没法测试 算求

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/delete")
    public void deleteExam(@RequestBody ExamResultDto exam)
    {
        examService.deleteExam(exam.getExamID());
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("/{examId}/results")
    public ExamResult getExamResults(@PathVariable Long examId) {
        return examService.getExamResults(examId);
    }
}