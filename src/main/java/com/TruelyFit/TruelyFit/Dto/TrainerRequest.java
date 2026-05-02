package com.TruelyFit.TruelyFit.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainerRequest {
	@NotNull(message="User ID is required")
	private Long userId;
    
	private String specialisation;
	private Integer experienceYears;
	private String bio;
	
}
