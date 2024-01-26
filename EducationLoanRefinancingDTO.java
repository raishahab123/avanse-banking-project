package com.avanse.springboot.DTO.forms.applyNow;

import lombok.Data;

@Data
public class EducationLoanRefinancingDTO {
	private Long id;
	private String name;
	private String email;
	private String phoneNumber;
	private String location;
	private Long balanceLoanAmount;
	
	private String universityAttending;
	private String studyingPlace;
	private String studyingCourse;
	
	private String graduateMonthAndYear;
	private Integer interestRate;
	
	

}
