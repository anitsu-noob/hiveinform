package com.example.hiveinform.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;       // 试卷名 - -》 用户的姓名
    private long exam_id ;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "paper_testing",
            joinColumns = @JoinColumn(name = "paper_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "testing_id", referencedColumnName = "id"))
    private List<Testing> questions;

    // 试卷
}