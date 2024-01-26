package com.avanse.springboot.DTO.forms.applyNow;
import lombok.Data;

@Data
public class SkillEnhancementDTO {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	private String city;
	private String courseName;
	private String courseProvider;
	private String loanAmount;

}
