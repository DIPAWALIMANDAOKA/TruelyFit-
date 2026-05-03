package com.TruelyFit.TruelyFit.Repository;
import com.TruelyFit.TruelyFit.Entity.Attendance;
import com.TruelyFit.TruelyFit.Enum.AttendaceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository 
        extends JpaRepository<Attendance, Long> {

    // Find all attendance for a specific member
    List<Attendance> findByMemberId(Long memberId);

    // Find attendance for a member on a specific date
    Optional<Attendance> findByMemberIdAndAttendanceDate(
            Long memberId, LocalDate date);

    // Check if attendance already marked for this member on this date
    boolean existsByMemberIdAndAttendanceDate(
            Long memberId, LocalDate date);

    // Find all attendance for a member in a date range
    List<Attendance> findByMemberIdAndAttendanceDateBetween(
            Long memberId, LocalDate startDate, LocalDate endDate);

    // Count present days for a member in a date range
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.member.id = :memberId " +
           "AND a.status = :status " +
           "AND a.attendanceDate BETWEEN :startDate AND :endDate")
    Long countByMemberIdAndStatusAndDateRange(
            @Param("memberId") Long memberId,
            @Param("status") AttendaceStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
