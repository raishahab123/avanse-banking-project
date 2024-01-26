package com.avanse.springboot.DTO.forms.contactUs;

import lombok.Data;

@Data
public class AssociateWithUsDTO {

	private Long id;
	private String name;
	private String contactNumber;
	private String email;
	private String city;
	private String partnershipType;
	
}
