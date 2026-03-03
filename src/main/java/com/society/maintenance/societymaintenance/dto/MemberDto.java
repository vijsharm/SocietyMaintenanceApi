package com.society.maintenance.societymaintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String name;
    private String flatNumber;
    private String email;
    private String phone;
    private Double monthlyAmount;
    private Double electricityArrear;
    private LocalDate onboardingDate;
}

