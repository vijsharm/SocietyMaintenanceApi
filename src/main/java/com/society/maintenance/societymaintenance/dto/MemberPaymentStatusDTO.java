package com.society.maintenance.societymaintenance.dto;

import lombok.Data;

@Data
public class MemberPaymentStatusDTO {
    private Long memberId;
    private String memberName;
    private String flatNumber;

    private Integer paidMonths;
    private Integer pendingMonths;

    private Double totalPaid;
    private Double totalPending;
}
