package com.example.hiveinform.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table
@Entity
public class Testing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;    // 题目
    private String content;  // 选项 或 标准答案
    private String answer;    // 答案
    private String attribute;    // 属性 例如 : "计算机组装" "硬件" "计算机科学等"
    private String type;      // 类型  "选择题" "问答题"
    // 题目
}