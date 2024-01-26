package com.avanse.springboot.DTO.forms.applyNow;

import lombok.Data;

@Data
public class PartnerWithUsDTO {
	private Long id;
	private String nameOfFirm;
	private String partnerType;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String email;
	private String city;
}
