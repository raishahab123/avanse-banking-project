package com.avanse.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avanse.springboot.model.Award;
import com.avanse.springboot.repository.AwardRepository;
import com.avanse.springboot.service.AwardService;

@RestController
public class AwardController {
	@Autowired
	AwardService awardService;
	
	@Autowired
	AwardRepository awardRepository;
	
	@GetMapping("public/api/getAllAwards")
	public List<Award> getAllAwards(){
		return awardService.getAllAwards();
	}

}
