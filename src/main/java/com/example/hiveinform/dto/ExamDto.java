package com.example.hiveinform.dto;

import com.example.hiveinform.entity.ExamResult;
import com.example.hiveinform.entity.Paper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamDto {
    private String name;
    private Long paperId;
    private Paper paper;
    private ExamResult results;
}
