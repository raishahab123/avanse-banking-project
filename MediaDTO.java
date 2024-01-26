package com.avanse.springboot.DTO.forms.contactUs;

import lombok.Data;

@Data
public class MediaDTO {
	
	private Long id;
	private String name;
	private String mediaHouse;
	private String phoneNumber;
	private String emailId;
	private String city;
	private String subjectLine;

}
