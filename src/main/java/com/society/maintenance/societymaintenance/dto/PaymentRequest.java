package com.society.maintenance.societymaintenance.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    public Long id;
    public Long memberId;
    public Double amount;
    public YearMonth month;          // "YYYY-MM"
    public LocalDate paymentDate;    // "YYYY-MM-DD"
    public String paymentMode;
    public String transactionId;

    public PaymentRequest(Long id, Long memberId, Double amount, YearMonth month,
                          LocalDate paymentDate, String paymentMode, String transactionId) {
        this.id = id;
        this.memberId = memberId;
        this.amount = amount;
        this.month = month;
        this.paymentDate = paymentDate;
        this.paymentMode = paymentMode;
        this.transactionId = transactionId;
        this.paymentType = "maintenance";
    }

    public String paymentType = "maintenance";
}

