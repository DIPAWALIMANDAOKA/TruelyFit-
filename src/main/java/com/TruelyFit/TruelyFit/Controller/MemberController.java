package com.TruelyFit.TruelyFit.Controller;

import com.TruelyFit.TruelyFit.Dto.MemberRequest;
import com.TruelyFit.TruelyFit.Dto.MemberResponse;
import com.TruelyFit.TruelyFit.Entity.User;
import com.TruelyFit.TruelyFit.Service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // POST /api/members — ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponse> addMember(
            @Valid @RequestBody MemberRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.addMember(request));
    }

    // GET /api/members — ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // GET /api/members/{id} — ADMIN only
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponse> getMemberById(
            @PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    // GET /api/members/my-profile — MEMBER only
    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MemberResponse> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(
                memberService.getMyProfile(currentUser.getId()));
    }

    // GET /api/members/trainer/{trainerId} — TRAINER only
    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<MemberResponse>> getMembersByTrainer(
            @PathVariable Long trainerId) {
        return ResponseEntity.ok(
                memberService.getMembersByTrainer(trainerId));
    }

    // PUT /api/members/{id} — ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(memberService.updateMember(id, request));
    }

    // DELETE /api/members/{id} — ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok("Member deleted successfully");
    }
}
