package com.TruelyFit.TruelyFit.Service;

import com.TruelyFit.TruelyFit.Dto.MemberRequest;
import com.TruelyFit.TruelyFit.Dto.MemberResponse;
import com.TruelyFit.TruelyFit.Entity.Member;
import com.TruelyFit.TruelyFit.Entity.Trainer;
import com.TruelyFit.TruelyFit.Entity.User;
import com.TruelyFit.TruelyFit.Exception.DuplicateRecordException;
import com.TruelyFit.TruelyFit.Exception.ResourceNotFoundException;
import com.TruelyFit.TruelyFit.Repository.MemberRepository;
import com.TruelyFit.TruelyFit.Repository.TrainerRepository;
import com.TruelyFit.TruelyFit.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;

    // Add new member — ADMIN only
    public MemberResponse addMember(MemberRequest request) {

        // Check user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getUserId()));

        // Check not already a member
        if (memberRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateRecordException("User is already a member");
        }

        // Build member
        Member member = Member.builder()
                .user(user)
                .goal(request.getGoal())
                .build();

        // Assign trainer if provided
        if (request.getTrainerId() != null) {
            Trainer trainer = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Trainer not found with id: " + request.getTrainerId()));
            member.setTrainer(trainer);
        }

        Member saved = memberRepository.save(member);
        return mapToResponse(saved);
    }

    // Get all members — ADMIN only
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get member by ID — ADMIN only
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with id: " + id));
        return mapToResponse(member);
    }

    // Get members by trainer — TRAINER only
    public List<MemberResponse> getMembersByTrainer(Long trainerId) {
        return memberRepository.findByTrainerId(trainerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get own profile — MEMBER only
    public MemberResponse getMyProfile(Long userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member profile not found for user: " + userId));
        return mapToResponse(member);
    }

    // Update member — ADMIN only
    public MemberResponse updateMember(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with id: " + id));

        member.setGoal(request.getGoal());

        if (request.getTrainerId() != null) {
            Trainer trainer = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Trainer not found with id: " + request.getTrainerId()));
            member.setTrainer(trainer);
        }

        return mapToResponse(memberRepository.save(member));
    }

    // Delete member — ADMIN only
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }

    // Map Entity to Response DTO
    private MemberResponse mapToResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .userId(member.getUser().getId())
                .userName(member.getUser().getName())
                .userEmail(member.getUser().getEmail())
                .goal(member.getGoal())
                .joinDate(member.getJoinDate())
                .status(member.getStatus())
                .trainerId(member.getTrainer() != null ?
                        member.getTrainer().getId() : null)
                .trainerName(member.getTrainer() != null ?
                        member.getTrainer().getUser().getName() : null)
                .build();
    }
}