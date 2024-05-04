package com.example.hiveinform.repository;

import com.example.hiveinform.entity.Carousel;
import com.example.hiveinform.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarouselRepository extends JpaRepository<Carousel, Long> {

}
