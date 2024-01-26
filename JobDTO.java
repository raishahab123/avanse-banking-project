package com.avanse.springboot.DTO;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JobDTO {

	private Long id;
	private String title;
	private Date postDate;
	private String shortDescription;
	private String description;
	private String postedBy;
	private String skills;
	
	private Boolean isJobActive;

	private String experienceInYears;

	private String location;
	private String jobCreatedDate;

}
