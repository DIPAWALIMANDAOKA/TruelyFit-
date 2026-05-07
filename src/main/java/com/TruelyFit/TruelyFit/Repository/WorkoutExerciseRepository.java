package com.TruelyFit.TruelyFit.Repository;

import com.TruelyFit.TruelyFit.Entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {

    // Find all exercises for a plan
    List<WorkoutExercise> findByWorkoutPlanIdOrderByExerciseOrder(Long workoutPlanId);

    // Check if exercise exists in a plan
    boolean existsByWorkoutPlanId(Long workoutPlanId);
}