package com.avanse.springboot.DTO;

import lombok.Data;

@Data
public class UserDTO {

	
	private Long id;
	private String firstName;	
	private String lastName;	
	private String email;
	private String password;
	private String dateOfBirth; 
	private String maritalStatus;
	private String gender;
	private String phoneNumber;
	private String  city;
}
