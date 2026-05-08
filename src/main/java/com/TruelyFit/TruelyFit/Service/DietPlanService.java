package com.TruelyFit.TruelyFit.Service;

import com.TruelyFit.TruelyFit.Dto.DietItemRequest;
import com.TruelyFit.TruelyFit.Dto.DietItemResponse;
import com.TruelyFit.TruelyFit.Dto.DietPlanRequest;
import com.TruelyFit.TruelyFit.Dto.DietPlanResponse;
import com.TruelyFit.TruelyFit.Entity.DietItem;
import com.TruelyFit.TruelyFit.Entity.DietPlan;
import com.TruelyFit.TruelyFit.Entity.Member;
import com.TruelyFit.TruelyFit.Entity.Trainer;
import com.TruelyFit.TruelyFit.Enum.DietPlanStatus;
import com.TruelyFit.TruelyFit.Exception.ResourceNotFoundException;
import com.TruelyFit.TruelyFit.Repository.DietItemRepository;
import com.TruelyFit.TruelyFit.Repository.DietPlanRepository;
import com.TruelyFit.TruelyFit.Repository.MemberRepository;
import com.TruelyFit.TruelyFit.Repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final DietItemRepository dietItemRepository;
    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;

    // Create diet plan — TRAINER only
    public DietPlanResponse createDietPlan(DietPlanRequest request) {

        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainer not found with id: " + request.getTrainerId()));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with id: " + request.getMemberId()));

        DietPlan plan = DietPlan.builder()
                .trainer(trainer)
                .member(member)
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? 
                        request.getStatus() : DietPlanStatus.ACTIVE)
                .build();

        // Add items if provided
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            List<DietItem> items = request.getItems()
                    .stream()
                    .map(itemReq -> DietItem.builder()
                            .dietPlan(plan)
                            .mealType(itemReq.getMealType())
                            .itemName(itemReq.getItemName())
                            .quantity(itemReq.getQuantity())
                            .calories(itemReq.getCalories())
                            .protein(itemReq.getProtein())
                            .carbs(itemReq.getCarbs())
                            .fats(itemReq.getFats())
                            .notes(itemReq.getNotes())
                            .itemOrder(itemReq.getItemOrder())
                            .build())
                    .collect(Collectors.toList());
            plan.setItems(items);
        }

        DietPlan saved = dietPlanRepository.save(plan);
        return mapToResponse(saved);
    }

    // Get plan by ID
    public DietPlanResponse getPlanById(Long id) {
        DietPlan plan = dietPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Diet plan not found with id: " + id));
        return mapToResponse(plan);
    }

    // Get member's plans
    public List<DietPlanResponse> getMemberPlans(Long memberId) {
        return dietPlanRepository.findByMemberId(memberId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get trainer's plans
    public List<DietPlanResponse> getTrainerPlans(Long trainerId) {
        return dietPlanRepository.findByTrainerId(trainerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get active plans for member
    public List<DietPlanResponse> getActivePlans(Long memberId) {
        return dietPlanRepository.findByMemberIdAndStatus(
                memberId, DietPlanStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update plan — TRAINER only
    public DietPlanResponse updatePlan(Long id, DietPlanRequest request) {
        DietPlan plan = dietPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Diet plan not found with id: " + id));

        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setStartDate(request.getStartDate());
        plan.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        return mapToResponse(dietPlanRepository.save(plan));
    }

    // Delete plan — TRAINER only
    public void deletePlan(Long id) {
        if (!dietPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Diet plan not found with id: " + id);
        }
        dietPlanRepository.deleteById(id);
    }

    // Add item to plan
    public DietItemResponse addItem(Long planId, DietItemRequest request) {
        DietPlan plan = dietPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Diet plan not found with id: " + planId));

        DietItem item = DietItem.builder()
                .dietPlan(plan)
                .mealType(request.getMealType())
                .itemName(request.getItemName())
                .quantity(request.getQuantity())
                .calories(request.getCalories())
                .protein(request.getProtein())
                .carbs(request.getCarbs())
                .fats(request.getFats())
                .notes(request.getNotes())
                .itemOrder(request.getItemOrder())
                .build();

        return mapItemToResponse(dietItemRepository.save(item));
    }

    // Get plan items
    public List<DietItemResponse> getPlanItems(Long planId) {
        if (!dietPlanRepository.existsById(planId)) {
            throw new ResourceNotFoundException(
                    "Diet plan not found with id: " + planId);
        }

        return dietItemRepository.findByDietPlanIdOrderByItemOrder(planId)
                .stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());
    }

    // Update item
    public DietItemResponse updateItem(Long itemId, DietItemRequest request) {
        DietItem item = dietItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Diet item not found with id: " + itemId));

        item.setMealType(request.getMealType());
        item.setItemName(request.getItemName());
        item.setQuantity(request.getQuantity());
        item.setCalories(request.getCalories());
        item.setProtein(request.getProtein());
        item.setCarbs(request.getCarbs());
        item.setFats(request.getFats());
        item.setNotes(request.getNotes());
        item.setItemOrder(request.getItemOrder());

        return mapItemToResponse(dietItemRepository.save(item));
    }

    // Delete item
    public void deleteItem(Long itemId) {
        if (!dietItemRepository.existsById(itemId)) {
            throw new ResourceNotFoundException(
                    "Diet item not found with id: " + itemId);
        }
        dietItemRepository.deleteById(itemId);
    }

    // Map entity to response with macro totals
    private DietPlanResponse mapToResponse(DietPlan plan) {
        Integer totalCalories = dietItemRepository.getTotalCalories(plan.getId());
        Double totalProtein = dietItemRepository.getTotalProtein(plan.getId());
        Double totalCarbs = dietItemRepository.getTotalCarbs(plan.getId());
        Double totalFats = dietItemRepository.getTotalFats(plan.getId());

        return DietPlanResponse.builder()
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
                .items(plan.getItems().stream()
                        .map(this::mapItemToResponse)
                        .collect(Collectors.toList()))
                .createdAt(plan.getCreatedAt())
                .totalItems(plan.getItems().size())
                .totalDailyCalories(totalCalories != null ? totalCalories : 0)
                .totalDailyProtein(totalProtein != null ? totalProtein : 0.0)
                .totalDailyCarbs(totalCarbs != null ? totalCarbs : 0.0)
                .totalDailyFats(totalFats != null ? totalFats : 0.0)
                .build();
    }

    private DietItemResponse mapItemToResponse(DietItem item) {
        return DietItemResponse.builder()
                .id(item.getId())
                .mealType(item.getMealType())
                .itemName(item.getItemName())
                .quantity(item.getQuantity())
                .calories(item.getCalories())
                .protein(item.getProtein())
                .carbs(item.getCarbs())
                .fats(item.getFats())
                .notes(item.getNotes())
                .itemOrder(item.getItemOrder())
                .build();
    }
}