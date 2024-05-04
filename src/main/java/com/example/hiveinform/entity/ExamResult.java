package com.example.hiveinform.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = {CascadeType.ALL})
    private Exam exam;   //  绑定考试
    @OneToOne(cascade = {CascadeType.ALL})
    private User user;
    private int score;
}