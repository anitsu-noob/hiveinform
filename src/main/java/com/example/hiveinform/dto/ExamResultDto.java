package com.example.hiveinform.dto;

import com.example.hiveinform.entity.Exam;
import com.example.hiveinform.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class ExamResultDto {
    private long examID;
    private long userID;
    private List<String> answer ;
    private int score;
}
