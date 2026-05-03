package com.TruelyFit.TruelyFit.Controller;
import com.TruelyFit.TruelyFit.Dto.AttendanceRequest;
import com.TruelyFit.TruelyFit.Dto.AttendanceResponse;
import com.TruelyFit.TruelyFit.Entity.User;
import com.TruelyFit.TruelyFit.Service.AttendanceService;
import com.TruelyFit.TruelyFit.Repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final MemberRepository memberRepository;

    // POST /api/attendance — mark attendance — TRAINER or ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<AttendanceResponse> markAttendance(
            @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attendanceService.markAttendance(request));
    }

    // GET /api/attendance/member/{memberId} — ADMIN + TRAINER
    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<AttendanceResponse>> getMemberAttendance(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(
                attendanceService.getMemberAttendance(memberId));
    }

    // GET /api/attendance/my — MEMBER sees own attendance
    @GetMapping("/my")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance(
            @AuthenticationPrincipal User currentUser) {

        // Get member ID from logged in user
        Long memberId = memberRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Member not found"))
                .getId();

        return ResponseEntity.ok(
                attendanceService.getMyAttendance(memberId));
    }

    // GET /api/attendance/member/{memberId}/range — date range filter
    @GetMapping("/member/{memberId}/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<AttendanceResponse>> getByDateRange(
            @PathVariable Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate endDate) {
        return ResponseEntity.ok(
                attendanceService.getAttendanceByDateRange(
                        memberId, startDate, endDate));
    }

    // GET /api/attendance/member/{memberId}/summary — attendance %
    @GetMapping("/member/{memberId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
    public ResponseEntity<String> getAttendanceSummary(
            @PathVariable Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate endDate) {
        return ResponseEntity.ok(
                attendanceService.getAttendanceSummary(
                        memberId, startDate, endDate));
    }

    // PUT /api/attendance/{id} — update attendance — ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendanceResponse> updateAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(
                attendanceService.updateAttendance(id, request));
    }
}