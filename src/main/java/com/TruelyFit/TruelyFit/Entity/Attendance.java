package com.TruelyFit.TruelyFit.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.TruelyFit.TruelyFit.Enum.AttendaceStatus;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    private AttendaceStatus status;

    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    @Column(name = "notes")
    private String notes;

    @PrePersist
    protected void onCreate() {
        markedAt = LocalDateTime.now();
    }
}
