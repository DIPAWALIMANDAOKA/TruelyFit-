package com.TruelyFit.TruelyFit.Dto;

import com.TruelyFit.TruelyFit.Enum.MealType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DietItemRequest {

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @NotNull(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Quantity is required")
    private String quantity;

    @NotNull(message = "Calories is required")
    @Min(value = 0, message = "Calories must be positive")
    private Integer calories;

    @NotNull(message = "Protein is required")
    @Min(value = 0, message = "Protein must be positive")
    private Double protein;

    @NotNull(message = "Carbs is required")
    @Min(value = 0, message = "Carbs must be positive")
    private Double carbs;

    @NotNull(message = "Fats is required")
    @Min(value = 0, message = "Fats must be positive")
    private Double fats;

    private String notes;

    private Integer itemOrder;
}