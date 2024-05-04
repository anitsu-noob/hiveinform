package com.example.hiveinform.dto;

import com.example.hiveinform.entity.Testing;
import lombok.Data;

import java.util.List;

@Data
public class PaperDto {
    private Long id;
    private String name;
    private List<Long> TestIds;
}
