package com.example.hiveinform.service;

import com.example.hiveinform.entity.Paper;

import java.util.List;

public interface PaperService {
    Paper addPaper(String name, List<Long> questionIds);
    void deletePaper(Long id);
    Paper updatePaper(Long id, String name, List<Long> questionIds);
    List<Paper> getAllPapers();

}
