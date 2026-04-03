package com.society.maintenance.societymaintenance.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkPaymentResponseDTO {

    private boolean success;
    private int count;
    private List<PaymentRequest> payments;
}
