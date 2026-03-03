package com.society.maintenance.societymaintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MemberStatementResponse {

    private MemberDto member;
    private List<PaymentRequest> payments;
    private PendingDues pendingDues;
}
