package com.avanse.springboot.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class JobSearchResponseDTO {
	int total;
	List<JobResponseDTO> result;

}
