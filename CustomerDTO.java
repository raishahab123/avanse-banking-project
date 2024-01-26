package com.avanse.springboot.DTO.forms.contactUs;

import lombok.Data;

@Data
public class CustomerDTO {
	
	private Long id;	
	private String name;
	private String phoneNumber;
	private String email;
	private String city;
	private String loanAccountNumber;
	private String loanStatus;

}
