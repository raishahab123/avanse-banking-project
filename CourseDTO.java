package com.avanse.springboot.DTO;
import java.util.Date;

import com.avanse.springboot.model.University;
import lombok.Data;

@Data
public class CourseDTO {
	
	private Long id;
	private String title;
	private String duration;
	private String month;
	private String description;
	private Double fees;
	private Date dateOfCreation;
	private String examsEligibility;
	private String documentsRequired;
	private String academicDocumentsRequired;
	private University university;
	private Boolean isCourseActive;
	private String staticContent;

	
}
