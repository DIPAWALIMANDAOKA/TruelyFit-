package com.TruelyFit.TruelyFit.Repository;

import com.TruelyFit.TruelyFit.Entity.DietPlan;
import com.TruelyFit.TruelyFit.Enum.DietPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {

    List<DietPlan> findByMemberId(Long memberId);

    List<DietPlan> findByTrainerId(Long trainerId);

    List<DietPlan> findByMemberIdAndStatus(Long memberId, DietPlanStatus status);

    List<DietPlan> findByMemberIdAndTrainerId(Long memberId, Long trainerId);
}
