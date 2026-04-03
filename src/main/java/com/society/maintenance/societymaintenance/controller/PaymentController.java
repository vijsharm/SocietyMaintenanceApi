package com.society.maintenance.societymaintenance.controller;

import com.society.maintenance.societymaintenance.dto.ApiResponse;
import com.society.maintenance.societymaintenance.dto.BulkPaymentRequest;
import com.society.maintenance.societymaintenance.dto.BulkPaymentResponseDTO;
import com.society.maintenance.societymaintenance.dto.PaymentRequest;
import com.society.maintenance.societymaintenance.entity.Payment;
import com.society.maintenance.societymaintenance.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ApiResponse<Payment> addPayment(@RequestBody PaymentRequest request) {
        service.addPayment(request);
        return ApiResponse.success("Payment recorded successfully", null);
    }

    @GetMapping
    public List<PaymentRequest> getPayments(
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        return service
                .getPayments(memberId, month, page, size)
                .map(this::mapToResponse).stream().toList();
    }

    private PaymentRequest mapToResponse(Payment payment) {
        return new PaymentRequest(
                payment.getId(),
                payment.getMember().getId(),
                payment.getAmount(),
                payment.getMonth(),
                payment.getPaymentDate(),
                payment.getPaymentMode(),
                payment.getTransactionId()
        );
    }

    @PostMapping("/bulk")
    public BulkPaymentResponseDTO createBulkPayments(
            @RequestBody BulkPaymentRequest request
    ) {
        var payments =  service.createBulkPayments(request);
        var responseList = payments.stream()
                .map(this::mapToResponse).toList();
        BulkPaymentResponseDTO response = new BulkPaymentResponseDTO();
        response.setSuccess(true);
        response.setCount(responseList.size());
        response.setPayments(responseList);
        return response;
    }
}
