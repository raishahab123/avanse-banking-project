package com.avanse.springboot.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePasswordDTO {
	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;
}
