package com.avanse.springboot.DTO;

import lombok.Data;

@Data
public class SearchResultDTO {
	private String heading;
	private String descriptionContainingKey;
	private String url;
	private String resultOrigin;
	

}
