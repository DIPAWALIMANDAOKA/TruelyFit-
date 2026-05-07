package com.TruelyFit.TruelyFit.Service;

import com.TruelyFit.TruelyFit.Dto.WorkoutExerciseRequest;
import com.TruelyFit.TruelyFit.Dto.WorkoutExerciseResponse;
import com.TruelyFit.TruelyFit.Dto.WorkoutPlanRequest;
import com.TruelyFit.TruelyFit.Dto.WorkoutPlanResponse;
import com.TruelyFit.TruelyFit.Entity.Member;
import com.TruelyFit.TruelyFit.Entity.Trainer;
import com.TruelyFit.TruelyFit.Entity.WorkoutExercise;
import com.TruelyFit.TruelyFit.Entity.WorkoutPlan;
import com.TruelyFit.TruelyFit.Enum.WorkoutPlanStatus;
import com.TruelyFit.TruelyFit.Exception.DuplicateRecordException;
import com.TruelyFit.TruelyFit.Exception.ResourceNotFoundException;
import com.TruelyFit.TruelyFit.Repository.MemberRepository;
import com.TruelyFit.TruelyFit.Repository.TrainerRepository;
import com.TruelyFit.TruelyFit.Repository.WorkoutExerciseRepository;
import com.TruelyFit.TruelyFit.Repository.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;

    // Create workout plan — TRAINER only
    public WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request) {

        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainer not found with id: " + request.getTrainerId()));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with id: " + request.getMemberId()));

        WorkoutPlan plan = WorkoutPlan.builder()
                .trainer(trainer)
                .member(member)
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? 
                        request.getStatus() : WorkoutPlanStatus.ACTIVE)
                .build();

        // Add exercises if provided
        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            List<WorkoutExercise> exercises = request.getExercises()
                    .stream()
                    .map(exerciseReq -> WorkoutExercise.builder()
                            .workoutPlan(plan)
                            .exerciseName(exerciseReq.getExerciseName())
                            .sets(exerciseReq.getSets())
                            .reps(exerciseReq.getReps())
                            .weightKg(exerciseReq.getWeightKg())
                            .durationMinutes(exerciseReq.getDurationMinutes())
                            .notes(exerciseReq.getNotes())
                            .exerciseOrder(exerciseReq.getExerciseOrder())
                            .build())
                    .collect(Collectors.toList());
            plan.setExercises(exercises);
        }

        WorkoutPlan saved = workoutPlanRepository.save(plan);
        return mapToResponse(saved);
    }

    // Get plan by ID
    public WorkoutPlanResponse getPlanById(Long id) {
        WorkoutPlan plan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan not found with id: " + id));
        return mapToResponse(plan);
    }

    // Get member's plans — MEMBER, TRAINER, ADMIN
    public List<WorkoutPlanResponse> getMemberPlans(Long memberId) {
        return workoutPlanRepository.findByMemberId(memberId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get trainer's plans — TRAINER, ADMIN
    public List<WorkoutPlanResponse> getTrainerPlans(Long trainerId) {
        return workoutPlanRepository.findByTrainerId(trainerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get active plans for member
    public List<WorkoutPlanResponse> getActivePlans(Long memberId) {
        return workoutPlanRepository.findByMemberIdAndStatus(
                memberId, WorkoutPlanStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update plan — TRAINER only
    public WorkoutPlanResponse updatePlan(Long id, WorkoutPlanRequest request) {
        WorkoutPlan plan = workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan not found with id: " + id));

        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setStartDate(request.getStartDate());
        plan.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        return mapToResponse(workoutPlanRepository.save(plan));
    }

    // Delete plan — TRAINER only
    public void deletePlan(Long id) {
        if (!workoutPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Workout plan not found with id: " + id);
        }
        workoutPlanRepository.deleteById(id);
    }

    // Add exercise to plan — TRAINER only
    public WorkoutExerciseResponse addExercise(Long planId, 
                                                WorkoutExerciseRequest request) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan not found with id: " + planId));

        WorkoutExercise exercise = WorkoutExercise.builder()
                .workoutPlan(plan)
                .exerciseName(request.getExerciseName())
                .sets(request.getSets())
                .reps(request.getReps())
                .weightKg(request.getWeightKg())
                .durationMinutes(request.getDurationMinutes())
                .notes(request.getNotes())
                .exerciseOrder(request.getExerciseOrder())
                .build();

        return mapExerciseToResponse(
                workoutExerciseRepository.save(exercise));
    }

    // Get exercises for a plan
    public List<WorkoutExerciseResponse> getPlanExercises(Long planId) {
        if (!workoutPlanRepository.existsById(planId)) {
            throw new ResourceNotFoundException(
                    "Workout plan not found with id: " + planId);
        }

        return workoutExerciseRepository
                .findByWorkoutPlanIdOrderByExerciseOrder(planId)
                .stream()
                .map(this::mapExerciseToResponse)
                .collect(Collectors.toList());
    }

    // Update exercise — TRAINER only
    public WorkoutExerciseResponse updateExercise(Long exerciseId,
                                                   WorkoutExerciseRequest request) {
        WorkoutExercise exercise = workoutExerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise not found with id: " + exerciseId));

        exercise.setExerciseName(request.getExerciseName());
        exercise.setSets(request.getSets());
        exercise.setReps(request.getReps());
        exercise.setWeightKg(request.getWeightKg());
        exercise.setDurationMinutes(request.getDurationMinutes());
        exercise.setNotes(request.getNotes());
        exercise.setExerciseOrder(request.getExerciseOrder());

        return mapExerciseToResponse(
                workoutExerciseRepository.save(exercise));
    }

    // Delete exercise — TRAINER only
    public void deleteExercise(Long exerciseId) {
        if (!workoutExerciseRepository.existsById(exerciseId)) {
            throw new ResourceNotFoundException(
                    "Exercise not found with id: " + exerciseId);
        }
        workoutExerciseRepository.deleteById(exerciseId);
    }

    // Map entity to response
    private WorkoutPlanResponse mapToResponse(WorkoutPlan plan) {
        return WorkoutPlanResponse.builder()
                .id(plan.getId())
                .trainerId(plan.getTrainer().getId())
                .trainerName(plan.getTrainer().getUser().getName())
                .memberId(plan.getMember().getId())
                .memberName(plan.getMember().getUser().getName())
                .name(plan.getName())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .status(plan.getStatus())
                .exercises(plan.getExercises().stream()
                        .map(this::mapExerciseToResponse)
                        .collect(Collectors.toList()))
                .createdAt(plan.getCreatedAt())
                .totalExercises(plan.getExercises().size())
                .build();
    }

    private WorkoutExerciseResponse mapExerciseToResponse(WorkoutExercise exercise) {
        return WorkoutExerciseResponse.builder()
                .id(exercise.getId())
                .exerciseName(exercise.getExerciseName())
                .sets(exercise.getSets())
                .reps(exercise.getReps())
                .weightKg(exercise.getWeightKg())
                .durationMinutes(exercise.getDurationMinutes())
                .notes(exercise.getNotes())
                .exerciseOrder(exercise.getExerciseOrder())
                .build();
    }
}