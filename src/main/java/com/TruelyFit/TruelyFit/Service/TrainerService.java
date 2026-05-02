package com.TruelyFit.TruelyFit.Service;

import com.TruelyFit.TruelyFit.Dto.TrainerRequest;
import com.TruelyFit.TruelyFit.Dto.TrainerResponse;
import com.TruelyFit.TruelyFit.Entity.Trainer;
import com.TruelyFit.TruelyFit.Entity.User;
import com.TruelyFit.TruelyFit.Repository.TrainerRepository;
import com.TruelyFit.TruelyFit.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    // Add trainer — ADMIN only
    public TrainerResponse addTrainer(TrainerRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + request.getUserId()));

        if (trainerRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("User is already a trainer");
        }

        Trainer trainer = Trainer.builder()
                .user(user)
                .specialisation(request.getSpecialisation())
                .experienceYears(request.getExperienceYears())
                .bio(request.getBio())
                .build();

        return mapToResponse(trainerRepository.save(trainer));
    }

    // Get all trainers — ADMIN only
    public List<TrainerResponse> getAllTrainers() {
        return trainerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get trainer by ID — ADMIN + MEMBER
    public TrainerResponse getTrainerById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Trainer not found with id: " + id));
        return mapToResponse(trainer);
    }

    // Get own profile — TRAINER only
    public TrainerResponse getMyProfile(Long userId) {
        Trainer trainer = trainerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException(
                        "Trainer profile not found"));
        return mapToResponse(trainer);
    }

    // Update trainer — ADMIN only
    public TrainerResponse updateTrainer(Long id, TrainerRequest request) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Trainer not found with id: " + id));

        trainer.setSpecialisation(request.getSpecialisation());
        trainer.setExperienceYears(request.getExperienceYears());
        trainer.setBio(request.getBio());

        return mapToResponse(trainerRepository.save(trainer));
    }

    // Delete trainer — ADMIN only
    public void deleteTrainer(Long id) {
        if (!trainerRepository.existsById(id)) {
            throw new RuntimeException("Trainer not found with id: " + id);
        }
        trainerRepository.deleteById(id);
    }

    // Map Entity to Response DTO
    private TrainerResponse mapToResponse(Trainer trainer) {
        return TrainerResponse.builder()
                .id(trainer.getId())
                .userId(trainer.getUser().getId())
                .name(trainer.getUser().getName())
                .email(trainer.getUser().getEmail())
                .specialisation(trainer.getSpecialisation())
                .experienceYears(trainer.getExperienceYears())
                .bio(trainer.getBio())
                .build();
    }
}