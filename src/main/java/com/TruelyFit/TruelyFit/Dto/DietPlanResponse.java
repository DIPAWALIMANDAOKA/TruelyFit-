package com.TruelyFit.TruelyFit.Dto;

import com.TruelyFit.TruelyFit.Enum.DietPlanStatus;
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
public class DietPlanResponse {
    private Long id;
    private Long trainerId;
    private String trainerName;
    private Long memberId;
    private String memberName;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private DietPlanStatus status;
    private List<DietItemResponse> items;
    private LocalDateTime createdAt;
    private Integer totalItems;
    private Integer totalDailyCalories;
    private Double totalDailyProtein;
    private Double totalDailyCarbs;
    private Double totalDailyFats;
}