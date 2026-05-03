package com.TruelyFit.TruelyFit.Dto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.TruelyFit.TruelyFit.Enum.AttendaceStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private Long id;
    private Long memberId;
    private String memberName;
    private LocalDate attendanceDate;
    private AttendaceStatus status;
    private LocalDateTime markedAt;
    private String notes;
}
