package com.society.maintenance.societymaintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PendingDuesResponse {

    private Long memberId;
    private String memberName;
    private String flatNumber;
    private List<PendingMonthDTO> pendingMonths;
    private double electricityArrear;
    private double totalPending;
}
