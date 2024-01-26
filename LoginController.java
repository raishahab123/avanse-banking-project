package com.avanse.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avanse.springboot.DTO.UpdatePasswordDTO;
import com.avanse.springboot.model.Role;
import com.avanse.springboot.model.User;
import com.avanse.springboot.repository.RoleRepository;
import com.avanse.springboot.repository.UserRepository;

@Controller
public class LoginController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String registerGet() {
		return "register";
	}
	
	
	/*
	 * Just adding the first Admin user manually and then later delete it
	 * 
	*/
	
	@GetMapping("/addManualUser")
	@ResponseBody
	public String addManualUser() {
		
		Role role = new Role();
		role.setName("ROLE_ADMIN");
		roleRepository.save(role);
		
		User user = new User(null,"Harish","Kumar","admin@avanse.com", bCryptPasswordEncoder.encode("avanse@123"), "22 March 2000","Single", "Male", "9812312312","Delhi",List.of(role));

		userRepository.save(user);
		
		return "Admin User Added Manually";
	}
	
	@GetMapping("/admin/update-password")
	public String updatePassword() {
		return "update-password";
	}
	
	@PostMapping(value = "/admin/update-password/change-action", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updatePasswordAction(@RequestBody UpdatePasswordDTO obj) {
		String currentPassword=obj.getCurrentPassword();
		String newPassword=obj.getNewPassword();
		String confirmNewPassword=obj.getConfirmNewPassword();
		User user = userRepository.findAll().get(0);
		if(bCryptPasswordEncoder.matches(currentPassword,user.getPassword())) {
			if(newPassword.equals(confirmNewPassword)) {
				user.setPassword(bCryptPasswordEncoder.encode(newPassword));
				userRepository.save(user);
				return "Password Updated";
			} else {
				return "New Password and the Confirm Password does not match";
			}
		} else {
			return "Unable to change the Password";
		}
	}
	
	
	
	
	/*
	 * We don't want to add any other user, 
	 * but just in case we need to, then we have can try adding on like this.
	 *  
	 *  
	 * @GetMapping("/addManualUser")
	 * 
	 * @ResponseBody public String addManualUser() {
	 * 
	 * Role role = new Role(); role.setName("ROLE_ADMIN");
	 * roleRepository.save(role);
	 * 
	 * User user = new User(null,"Anurag","Kumar","admin@gmail.com",
	 * bCryptPasswordEncoder.encode("password"), "22 March 2000","Single", "Male",
	 * "9812312312","Delhi",List.of(role));
	 * 
	 * userRepository.save(user);
	 * 
	 * return "Admin User Added Manually"; }
	 * 
	 */	
	
}
