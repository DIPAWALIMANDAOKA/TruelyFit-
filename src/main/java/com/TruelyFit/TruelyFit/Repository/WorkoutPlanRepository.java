package com.TruelyFit.TruelyFit.Repository;

import com.TruelyFit.TruelyFit.Entity.WorkoutPlan;
import com.TruelyFit.TruelyFit.Enum.WorkoutPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    // Find all plans for a specific member
    List<WorkoutPlan> findByMemberId(Long memberId);

    // Find all plans created by a trainer
    List<WorkoutPlan> findByTrainerId(Long trainerId);

    // Find all active plans for a member
    List<WorkoutPlan> findByMemberIdAndStatus(Long memberId, WorkoutPlanStatus status);

    // Find all plans for a member from a specific trainer
    List<WorkoutPlan> findByMemberIdAndTrainerId(Long memberId, Long trainerId);
}