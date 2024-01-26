package com.avanse.springboot.DTO;



import java.util.Date;

import lombok.Data;

@Data
public class UniversityDTO {

	private Long id;
	private String name;
	private String location;
	private String imageName;
	private String description;
	private Integer establishedYear;
	private Date dateOfCreation;

	
	private String accomodation;
	private String intakePeriod;
	private String applicationProcess;
	private Boolean isUniversityActive;
	
	private String staticContent;


}
