package com.TruelyFit.TruelyFit.Dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class WorkoutExerciseRequest {

    @NotNull(message = "Exercise name is required")
    private String exerciseName;

    @NotNull(message = "Sets is required")
    @Min(value = 1, message = "Sets must be at least 1")
    private Integer sets;

    @NotNull(message = "Reps is required")
    @Min(value = 1, message = "Reps must be at least 1")
    private Integer reps;

    private Double weightKg;

    private Integer durationMinutes;

    private String notes;

    private Integer exerciseOrder;
}