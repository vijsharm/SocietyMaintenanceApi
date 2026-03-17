package com.society.maintenance.societymaintenance.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportsResponseDTO {
    private List<MonthlyCollectionDTO> monthlyCollection;
    private List<MemberPaymentStatusDTO> memberPaymentStatus;
}
