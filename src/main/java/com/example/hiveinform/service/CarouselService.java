package com.example.hiveinform.service;

import com.example.hiveinform.entity.Carousel;

import java.util.List;

public interface CarouselService {
    List<Carousel> getCarouselList() ;

    Carousel createCarousel(Carousel carousel);

    Boolean deleteCarousel(long id);

    Carousel updateCarousel(Carousel carousel);
}
