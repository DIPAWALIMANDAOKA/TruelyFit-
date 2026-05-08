package com.TruelyFit.TruelyFit.Controller;
import com.TruelyFit.TruelyFit.Dto.DietItemRequest;
import com.TruelyFit.TruelyFit.Dto.DietItemResponse;
import com.TruelyFit.TruelyFit.Dto.DietPlanRequest;
import com.TruelyFit.TruelyFit.Dto.DietPlanResponse;
import com.TruelyFit.TruelyFit.Service.DietPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/diet-plans")
@RequiredArgsConstructor
public class DietPlanController {

    private final DietPlanService dietPlanService;

    // POST /api/diet-plans — TRAINER only
    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<DietPlanResponse> createPlan(
            @Valid @RequestBody DietPlanRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dietPlanService.createDietPlan(request));
    }

    // GET /api/diet-plans/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<DietPlanResponse> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(dietPlanService.getPlanById(id));
    }

    // GET /api/diet-plans/member/{memberId}
    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<List<DietPlanResponse>> getMemberPlans(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(dietPlanService.getMemberPlans(memberId));
    }

    // GET /api/diet-plans/trainer/{trainerId}
    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<DietPlanResponse>> getTrainerPlans(
            @PathVariable Long trainerId) {
        return ResponseEntity.ok(dietPlanService.getTrainerPlans(trainerId));
    }

    // GET /api/diet-plans/member/{memberId}/active
    @GetMapping("/member/{memberId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<List<DietPlanResponse>> getActivePlans(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(dietPlanService.getActivePlans(memberId));
    }

    // PUT /api/diet-plans/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<DietPlanResponse> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody DietPlanRequest request) {
        return ResponseEntity.ok(dietPlanService.updatePlan(id, request));
    }

    // DELETE /api/diet-plans/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<String> deletePlan(@PathVariable Long id) {
        dietPlanService.deletePlan(id);
        return ResponseEntity.ok("Diet plan deleted successfully");
    }

    // POST /api/diet-plans/{planId}/items
    @PostMapping("/{planId}/items")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<DietItemResponse> addItem(
            @PathVariable Long planId,
            @Valid @RequestBody DietItemRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dietPlanService.addItem(planId, request));
    }

    // GET /api/diet-plans/{planId}/items
    @GetMapping("/{planId}/items")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<List<DietItemResponse>> getPlanItems(
            @PathVariable Long planId) {
        return ResponseEntity.ok(dietPlanService.getPlanItems(planId));
    }

    // PUT /api/diet-plans/items/{itemId}
    @PutMapping("/items/{itemId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<DietItemResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody DietItemRequest request) {
        return ResponseEntity.ok(dietPlanService.updateItem(itemId, request));
    }

    // DELETE /api/diet-plans/items/{itemId}
    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        dietPlanService.deleteItem(itemId);
        return ResponseEntity.ok("Diet item deleted successfully");
    }
}
