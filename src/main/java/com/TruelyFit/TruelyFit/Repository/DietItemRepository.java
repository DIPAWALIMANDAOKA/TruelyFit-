package com.TruelyFit.TruelyFit.Repository;
import com.TruelyFit.TruelyFit.Entity.DietItem;
import com.TruelyFit.TruelyFit.Enum.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DietItemRepository extends JpaRepository<DietItem, Long> {

    List<DietItem> findByDietPlanIdOrderByItemOrder(Long dietPlanId);

    List<DietItem> findByDietPlanIdAndMealType(Long dietPlanId, MealType mealType);

    boolean existsByDietPlanId(Long dietPlanId);

    @Query("SELECT SUM(d.calories) FROM DietItem d WHERE d.dietPlan.id = :planId")
    Integer getTotalCalories(@Param("planId") Long planId);

    @Query("SELECT SUM(d.protein) FROM DietItem d WHERE d.dietPlan.id = :planId")
    Double getTotalProtein(@Param("planId") Long planId);

    @Query("SELECT SUM(d.carbs) FROM DietItem d WHERE d.dietPlan.id = :planId")
    Double getTotalCarbs(@Param("planId") Long planId);

    @Query("SELECT SUM(d.fats) FROM DietItem d WHERE d.dietPlan.id = :planId")
    Double getTotalFats(@Param("planId") Long planId);
}