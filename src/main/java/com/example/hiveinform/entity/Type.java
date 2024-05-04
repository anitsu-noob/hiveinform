package com.example.hiveinform.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String hobby;

    private long userId ;
    // 用户的爱好以及属性 属于主动收集的信息 不由用户自己添加 由系统收集
}

