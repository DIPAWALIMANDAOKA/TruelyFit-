package com.TruelyFit.TruelyFit.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainerResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String specialisation;
    private Integer experienceYears;
    private String bio;

}
