package com.avanse.springboot.DTO.forms.applyNow;

import lombok.Data;

@Data
public class ExecutiveEducationLoanDTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	private String courseName;
	private String loanAmount;
}
