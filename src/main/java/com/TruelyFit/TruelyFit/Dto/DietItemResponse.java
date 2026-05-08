package com.TruelyFit.TruelyFit.Dto;

import com.TruelyFit.TruelyFit.Enum.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietItemResponse {
    private Long id;
    private MealType mealType;
    private String itemName;
    private String quantity;
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fats;
    private String notes;
    private Integer itemOrder;
}