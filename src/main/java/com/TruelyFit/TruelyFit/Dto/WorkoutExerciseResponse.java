package com.TruelyFit.TruelyFit.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseResponse {
    private Long id;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Double weightKg;
    private Integer durationMinutes;
    private String notes;
    private Integer exerciseOrder;
}