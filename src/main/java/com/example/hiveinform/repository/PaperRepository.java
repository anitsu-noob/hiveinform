package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PaperRepository extends JpaRepository<Paper, Long> {
}
