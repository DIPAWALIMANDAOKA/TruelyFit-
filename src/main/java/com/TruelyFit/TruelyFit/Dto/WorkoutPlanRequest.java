package com.TruelyFit.TruelyFit.Dto;

import com.TruelyFit.TruelyFit.Enum.WorkoutPlanStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutPlanRequest {

    @NotNull(message = "Trainer ID is required")
    private Long trainerId;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Plan name is required")
    private String name;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    private WorkoutPlanStatus status;

    private List<WorkoutExerciseRequest> exercises;
}