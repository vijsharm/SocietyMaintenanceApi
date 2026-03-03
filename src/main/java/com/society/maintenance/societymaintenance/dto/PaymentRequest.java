package com.society.maintenance.societymaintenance.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@AllArgsConstructor
public class PaymentRequest {

    public Long id;
    public Long memberId;
    public Double amount;
    public YearMonth month;          // "YYYY-MM"
    public LocalDate paymentDate;    // "YYYY-MM-DD"
    public String paymentMode;
    public String transactionId;
}

