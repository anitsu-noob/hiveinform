package com.example.hiveinform.controller;

import com.example.hiveinform.dto.TestingDto;
import com.example.hiveinform.entity.Testing;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.service.PaperService;
import com.example.hiveinform.service.TestingService;
import com.example.hiveinform.util.CsvExportOfTesting;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//@Secured({"ROLE_USER","ROLE_ADMIN", "SUPER_ADMIN"})
@RestController
@RequestMapping("/testing")
public class TestingController {

    @GetMapping
    public String testings() {
        return "Hello" ;
    }
    @Autowired
    private TestingService questionService;

    @Autowired
    private PaperService paperService ;

    @Secured({"ROLE_ADMIN","ROLE_SUPERADMIN"})
    @PostMapping("/add")
    public Testing addQuestion(@RequestBody TestingDto testDto) {
        return questionService.addQuestion(testDto);
    }

    @Secured({"ROLE_SUPERADMIN","ROLE_ADMIN"})
    @PostMapping("/delete")
    public void deleteQuestion(@RequestBody Long id) {
        questionService.deleteQuestion(id);
    }

    @Secured({"ROLE_SUPERADMIN","ROLE_ADMIN"})
    @PostMapping("/update")
    public Testing updateQuestion(@RequestBody Testing testing) {
        return questionService.updateQuestion(testing);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("/get/{username}/{type}")
    public List<Testing> getAllQuestions(@PathVariable String username,@PathVariable String type) {
        List<Testing> questions = questionService.getQuestionByType(type);
        List<Long> questionsId = new ArrayList<Long>();
        for (Testing question : questions) {
            questionsId.add(question.getId());
        }
        paperService.addPaper(username,questionsId);
        return questions;
    }

    @Secured({"ROLE_SUPERADMIN","ROLE_ADMIN"})
    @GetMapping("/export/getAllByCsv")
    public void exportAllByCsv(HttpServletResponse response)
    {
        String[] head = {"id", "title", "content", "answer", "attribute", "type"};
        String tableHead = String.join(",", head);
        List<Testing> questions = questionService.getAllQuestions();
        CsvExportOfTesting csvExportOfTesting = new CsvExportOfTesting();
        String fileName = "questions";
        Boolean isFile = false;
        try {
            isFile = csvExportOfTesting.createAndWriteCSV(fileName, questions, tableHead);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (isFile) {
            response.setHeader("Content-Type", "text/csv;charset=UTF-8");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"testing.csv\"");
            Path filePath = Paths.get("E:/var", "questions.csv");
            try (InputStream inputStream = new FileInputStream(filePath.toString())) {
                inputStream.transferTo(response.getOutputStream());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
            return;
    }
    // 下载

    @Secured({"ROLE_SUPERADMIN","ROLE_ADMIN"})
    @PostMapping("/upload")
    public List<TestingDto> upload(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        List<TestingDto> testings = new ArrayList<>();
        if ("text/csv".equals(multipartFile.getContentType()))
        {
            try (CSVParser parser = new CSVParser( new InputStreamReader(multipartFile.getInputStream(),StandardCharsets.UTF_8),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader()))
            {
                 for (CSVRecord record : parser.getRecords())
                 {
                    TestingDto question = new TestingDto();
                    question.setType(record.get("type"));
                    question.setAnswer(record.get("answer"));
                    question.setAttribute(record.get("attribute"));
                    question.setTitle(record.get("title"));
                    question.setContent(record.get("content"));
                    questionService.addQuestion(question);
                    testings.add(question);
                 }
                 return testings;
            }
        }
        throw new RuntimeException("Invalid multipart file has errors , please check it ");
    }
    // 上传
}
