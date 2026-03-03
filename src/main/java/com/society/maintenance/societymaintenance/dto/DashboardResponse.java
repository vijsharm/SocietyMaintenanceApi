package com.society.maintenance.societymaintenance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private Long totalMembers;
    private Double collectedThisMonth;
    private Double expectedThisMonth;
    private Double pendingThisMonth;
    private Long paymentsThisMonth;
    private Double societyBalance;
    private Double totalExpenditure;
    private Double totalPendingAllMonths;
}
