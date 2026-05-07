package com.TruelyFit.TruelyFit.Controller;

import com.TruelyFit.TruelyFit.Dto.WorkoutExerciseRequest;
import com.TruelyFit.TruelyFit.Dto.WorkoutExerciseResponse;
import com.TruelyFit.TruelyFit.Dto.WorkoutPlanRequest;
import com.TruelyFit.TruelyFit.Dto.WorkoutPlanResponse;
import com.TruelyFit.TruelyFit.Service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    // POST /api/workout-plans — create plan — TRAINER only
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutPlanResponse> createPlan(
            @Valid @RequestBody WorkoutPlanRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workoutPlanService.createWorkoutPlan(request));
    }

    // GET /api/workout-plans/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<WorkoutPlanResponse> getPlanById(
            @PathVariable Long id) {
        return ResponseEntity.ok(workoutPlanService.getPlanById(id));
    }

    // GET /api/workout-plans/member/{memberId} — MEMBER, TRAINER, ADMIN
    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<List<WorkoutPlanResponse>> getMemberPlans(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(workoutPlanService.getMemberPlans(memberId));
    }

    // GET /api/workout-plans/trainer/{trainerId} — TRAINER, ADMIN
    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<WorkoutPlanResponse>> getTrainerPlans(
            @PathVariable Long trainerId) {
        return ResponseEntity.ok(workoutPlanService.getTrainerPlans(trainerId));
    }

    // GET /api/workout-plans/member/{memberId}/active
    @GetMapping("/member/{memberId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<List<WorkoutPlanResponse>> getActivePlans(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(workoutPlanService.getActivePlans(memberId));
    }

    // PUT /api/workout-plans/{id} — TRAINER only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutPlanResponse> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutPlanRequest request) {
        return ResponseEntity.ok(workoutPlanService.updatePlan(id, request));
    }

    // DELETE /api/workout-plans/{id} — TRAINER only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<String> deletePlan(@PathVariable Long id) {
        workoutPlanService.deletePlan(id);
        return ResponseEntity.ok("Workout plan deleted successfully");
    }

    // POST /api/workout-plans/{planId}/exercises — add exercise
    @PostMapping("/{planId}/exercises")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutExerciseResponse> addExercise(
            @PathVariable Long planId,
            @Valid @RequestBody WorkoutExerciseRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workoutPlanService.addExercise(planId, request));
    }

    // GET /api/workout-plans/{planId}/exercises
    @GetMapping("/{planId}/exercises")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<List<WorkoutExerciseResponse>> getPlanExercises(
            @PathVariable Long planId) {
        return ResponseEntity.ok(workoutPlanService.getPlanExercises(planId));
    }

    // PUT /api/workout-plans/exercises/{exerciseId}
    @PutMapping("/exercises/{exerciseId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<WorkoutExerciseResponse> updateExercise(
            @PathVariable Long exerciseId,
            @Valid @RequestBody WorkoutExerciseRequest request) {
        return ResponseEntity.ok(
                workoutPlanService.updateExercise(exerciseId, request));
    }

    // DELETE /api/workout-plans/exercises/{exerciseId}
    @DeleteMapping("/exercises/{exerciseId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<String> deleteExercise(
            @PathVariable Long exerciseId) {
        workoutPlanService.deleteExercise(exerciseId);
        return ResponseEntity.ok("Exercise deleted successfully");
    }
}