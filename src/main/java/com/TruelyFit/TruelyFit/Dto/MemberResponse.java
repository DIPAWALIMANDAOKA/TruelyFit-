package com.TruelyFit.TruelyFit.Dto;

import java.time.LocalDate;

import com.TruelyFit.TruelyFit.Enum.Goal;
import com.TruelyFit.TruelyFit.Enum.MemberStatus;

public class MemberResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long trainerId;
    private String trainerName;
    private Goal goal;
    private LocalDate joinDate;
    private MemberStatus status;
}
