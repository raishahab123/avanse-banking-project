package com.avanse.springboot.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class ImageDTO {

	private Long id;
	private String name;
	private String fileName;
	private String link;
	private String altTag;
	private int height;
	private int width;
	private Date uploadedOn;
	private Long size;
	
}
