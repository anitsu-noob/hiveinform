package com.example.hiveinform.controller;

import com.example.hiveinform.dto.PaperDto;
import com.example.hiveinform.entity.Paper;
import com.example.hiveinform.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
@RestController
@RequestMapping("/paper")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @Secured({"ROLE_SUPERADMIN","ROLE_ADMIN"})
    @PostMapping("/add")
    public Paper addPaper(@RequestBody PaperDto paperDto) {
        return paperService.addPaper(paperDto.getName() ,paperDto.getTestIds());
    }

    @Secured({"ROLE_SUPERADMIN","ROLE_ADMIN"})
    @PostMapping("/delete")
    public void deletePaper(@RequestParam Long id) {
        paperService.deletePaper(id);
    }

    @Secured({"ROLE_SUPERADMIN","ROLE_ADMIN"})
    @PostMapping("/update")
    public Paper updatePaper(@RequestBody PaperDto paperDto) {
        return paperService.updatePaper(paperDto.getId(), paperDto.getName(), paperDto.getTestIds());
    }

    @GetMapping("/all")
    public List<Paper> getAllPapers() {
        return paperService.getAllPapers();
    }
}