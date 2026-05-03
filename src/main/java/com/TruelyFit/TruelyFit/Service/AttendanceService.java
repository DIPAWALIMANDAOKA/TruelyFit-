package com.TruelyFit.TruelyFit.Service;

import com.TruelyFit.TruelyFit.Dto.AttendanceRequest;
import com.TruelyFit.TruelyFit.Dto.AttendanceResponse;
import com.TruelyFit.TruelyFit.Entity.Attendance;
import com.TruelyFit.TruelyFit.Entity.Member;
import com.TruelyFit.TruelyFit.Enum.AttendaceStatus;
import com.TruelyFit.TruelyFit.Repository.AttendanceRepository;
import com.TruelyFit.TruelyFit.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    // Mark attendance — TRAINER or ADMIN
    public AttendanceResponse markAttendance(AttendanceRequest request) {

        // Step 1 — Does this member exist?
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException(
                        "Member not found with id: " + request.getMemberId()));

        // Step 2 — Already marked today?
        if (attendanceRepository.existsByMemberIdAndAttendanceDate(
                request.getMemberId(), request.getAttendanceDate())) {
            throw new RuntimeException(
                    "Attendance already marked for this member on: "
                    + request.getAttendanceDate());
        }

        // Step 3 — Build and save
        Attendance attendance = Attendance.builder()
                .member(member)
                .attendanceDate(request.getAttendanceDate())
                .status(request.getStatus())
                .notes(request.getNotes())
                .build();

        return mapToResponse(attendanceRepository.save(attendance));
    }

    // Get all attendance for a member — ADMIN + TRAINER
    public List<AttendanceResponse> getMemberAttendance(Long memberId) {
        return attendanceRepository.findByMemberId(memberId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get own attendance — MEMBER only
    public List<AttendanceResponse> getMyAttendance(Long memberId) {
        return attendanceRepository.findByMemberId(memberId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get attendance in date range — ADMIN + TRAINER
    public List<AttendanceResponse> getAttendanceByDateRange(
            Long memberId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository
                .findByMemberIdAndAttendanceDateBetween(
                        memberId, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get attendance summary — how many days present this month
    public String getAttendanceSummary(Long memberId,
                                        LocalDate startDate,
                                        LocalDate endDate) {
        Long presentDays = attendanceRepository
                .countByMemberIdAndStatusAndDateRange(
                        memberId,
                        AttendaceStatus.PRESENT,
                        startDate,
                        endDate);

        long totalDays = startDate.until(endDate).getDays() + 1;
        double percentage = (presentDays * 100.0) / totalDays;

        return String.format(
                "Present: %d days out of %d days (%.1f%%)",
                presentDays, totalDays, percentage);
    }

    // Update attendance — ADMIN only
    public AttendanceResponse updateAttendance(
            Long id, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Attendance record not found with id: " + id));

        attendance.setStatus(request.getStatus());
        attendance.setNotes(request.getNotes());

        return mapToResponse(attendanceRepository.save(attendance));
    }

    // Map entity to response DTO
    private AttendanceResponse mapToResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .memberId(attendance.getMember().getId())
                .memberName(attendance.getMember().getUser().getName())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .markedAt(attendance.getMarkedAt())
                .notes(attendance.getNotes())
                .build();
    }
}