package com.avanse.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avanse.springboot.model.Testimonial;
import com.avanse.springboot.repository.TestimonialRepository;
import com.avanse.springboot.service.TestimonialService;

@RestController
public class TestimonialController {
	
	@Autowired
	TestimonialService testimonialService;
	
	@Autowired
	TestimonialRepository testimonialRepository;
	
	@GetMapping("/public/api/getAllTestimonials")
	public List<Testimonial> getAllTestimonials() {
		return testimonialService.getAllTestimonials();
	}
	

}
