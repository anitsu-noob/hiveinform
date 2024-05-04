package com.example.hiveinform.service.impl;

import com.example.hiveinform.entity.Carousel;
import com.example.hiveinform.repository.CarouselRepository;
import com.example.hiveinform.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("carouselService")
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselRepository carouselRepository ;

    @Override
    public List<Carousel> getCarouselList() {
        return carouselRepository.findAll();
    }

    @Override
    public Carousel createCarousel(Carousel carousel) {
        carousel.setId(null);
        return carouselRepository.save(carousel);
    }

    @Override
    public Boolean deleteCarousel(long id) {
        Carousel carousel = carouselRepository.findById(id).orElse(null);
        if (carousel != null) {
            carouselRepository.delete(carousel);
            return  true;
        }
        else  return false;
    }

    @Override
    public Carousel updateCarousel(Carousel carousel) {
        Carousel carousel1 = carouselRepository.findById(carousel.getId()).orElse(null);
        if (carousel1!=null) {
            carousel1.setDescription(carousel.getDescription());
            carousel1.setImageId(carousel.getImageId());
            carousel1.setLink(carousel.getLink());
            carousel1.setTitle(carousel.getTitle());
            carousel1.setImageUrl(carousel.getImageUrl());
            return carouselRepository.save(carousel1);
        }
        else
            throw new RuntimeException("已被删除或存在");
    }
}
