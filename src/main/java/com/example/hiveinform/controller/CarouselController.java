package com.example.hiveinform.controller;

import com.example.hiveinform.entity.Carousel;
import com.example.hiveinform.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping
    public List<Carousel> getCarouselList() {
        return carouselService.getCarouselList();
    }

    @Secured({"ROLE_ADMIN","ROLE_SUPERADMIN"})
    @PostMapping
    public Carousel createCarousel(@RequestBody Carousel carousel) {
        return carouselService.createCarousel(carousel);
    }

    @Secured({"ROLE_ADMIN","ROLE_SUPERADMIN"})
    @GetMapping("/del/{id}")
    public boolean deleteCarousel(@PathVariable long id)
    {
        return carouselService.deleteCarousel(id);
    }

    @Secured({"ROLE_ADMIN","ROLE_SUPERADMIN"})
    @PostMapping("/update")
    public Carousel changeCarouse(@RequestBody Carousel carousel) {
        return carouselService.updateCarousel(carousel);
    }
}
