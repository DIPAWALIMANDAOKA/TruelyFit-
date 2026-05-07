package com.TruelyFit.TruelyFit.Dto;

import com.TruelyFit.TruelyFit.Enum.WorkoutPlanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanResponse {
    private Long id;
    private Long trainerId;
    private String trainerName;
    private Long memberId;
    private String memberName;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private WorkoutPlanStatus status;
    private List<WorkoutExerciseResponse> exercises;
    private LocalDateTime createdAt;
    private Integer totalExercises;
}