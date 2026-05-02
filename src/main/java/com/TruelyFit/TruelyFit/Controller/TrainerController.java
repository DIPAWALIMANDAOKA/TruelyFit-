package com.TruelyFit.TruelyFit.Controller;

import com.TruelyFit.TruelyFit.Dto.TrainerRequest;
import com.TruelyFit.TruelyFit.Dto.TrainerResponse;
import com.TruelyFit.TruelyFit.Entity.User;
import com.TruelyFit.TruelyFit.Service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    // POST /api/trainers — ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponse> addTrainer(
            @Valid @RequestBody TrainerRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(trainerService.addTrainer(request));
    }

    // GET /api/trainers — ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrainerResponse>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    // GET /api/trainers/{id} — ADMIN + MEMBER
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<TrainerResponse> getTrainerById(
            @PathVariable Long id) {
        return ResponseEntity.ok(trainerService.getTrainerById(id));
    }

    // GET /api/trainers/my-profile — TRAINER only
    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<TrainerResponse> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(
                trainerService.getMyProfile(currentUser.getId()));
    }

    // PUT /api/trainers/{id} — ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrainerResponse> updateTrainer(
            @PathVariable Long id,
            @Valid @RequestBody TrainerRequest request) {
        return ResponseEntity.ok(
                trainerService.updateTrainer(id, request));
    }

    // DELETE /api/trainers/{id} — ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok("Trainer deleted successfully");
    }
}
