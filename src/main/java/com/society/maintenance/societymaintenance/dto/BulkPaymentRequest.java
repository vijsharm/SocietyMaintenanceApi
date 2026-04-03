package com.society.maintenance.societymaintenance.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkPaymentRequest {

    private List<PaymentRequest> payments;

}
