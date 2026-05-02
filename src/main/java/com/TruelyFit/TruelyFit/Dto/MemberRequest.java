package com.TruelyFit.TruelyFit.Dto;

import com.TruelyFit.TruelyFit.Enum.Goal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberRequest {
	@NotNull(message = "User ID is required")
    private Long userId;

    private Long trainerId;

    @NotNull(message = "Goal is required")
    private Goal goal;
}
