package com.TruelyFit.TruelyFit.Entity;

import jakarta.persistence.*;
import lombok.*;
import com.TruelyFit.TruelyFit.Enum.MealType;

@Entity
@Table(name = "diet_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diet_plan_id", nullable = false)
    private DietPlan dietPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType mealType;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String quantity;

    @Column(nullable = false)
    private Integer calories;

    @Column(nullable = false)
    private Double protein;

    @Column(nullable = false)
    private Double carbs;

    @Column(nullable = false)
    private Double fats;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "item_order")
    private Integer itemOrder;
}