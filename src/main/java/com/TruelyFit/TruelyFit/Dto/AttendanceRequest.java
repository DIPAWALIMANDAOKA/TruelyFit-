package com.TruelyFit.TruelyFit.Dto;

import java.time.LocalDate;

import com.TruelyFit.TruelyFit.Enum.AttendaceStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceRequest {

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Date is required")
    private LocalDate attendanceDate;

    @NotNull(message = "Status is required")
    private AttendaceStatus status;

    private String notes;
}