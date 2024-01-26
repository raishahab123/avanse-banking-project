package com.avanse.springboot.DTO.forms.applyNow;

import java.util.Date;

import lombok.Data;

@Data
public class ApplyNowGeneralDTO {
	
	private Integer id;
	private String name;
	private String contactNumber;
	private String emailId;
	private String city;
	private String placeOfStudy;
	private String loanAmount;
	private String admissionStatus;
	private String permission;
	private String timeOfStudy;
	private String create;
	private String source;

	private String campaignid;
	private String adgroup;
	private String keywords;
	private String gclid;
	private String Ip_address;
//	Date has to be implemented later
//	private Date timeOfStudy;
}
