package com.TruelyFit.TruelyFit.Dto;
import com.TruelyFit.TruelyFit.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RegisterRequest {

	@NotBlank(message = "Password is required")
	private String password ;
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email")
	private String email;
	
	@NotNull(message = "Role is required")
	private Role role;
}
