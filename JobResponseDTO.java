package com.avanse.springboot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class JobResponseDTO {
	String jobId;
	String jobTitle;
	String jobDescription;
	String jobDescriptionV2;
	String jobCode;
	String rolesAndResponsibilities;
	String location;
	String applyUrl;

}
