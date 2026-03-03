package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.PaymentRequest;
import com.society.maintenance.societymaintenance.entity.Member;
import com.society.maintenance.societymaintenance.entity.Payment;
import com.society.maintenance.societymaintenance.repository.MemberRepository;
import com.society.maintenance.societymaintenance.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;


    public void addPayment(PaymentRequest request) {

        Member member = memberRepository.findById(request.memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Payment payment = new Payment();
        payment.setMember(member);
        payment.setAmount(request.amount);
        payment.setMonth(request.month);
        payment.setPaymentMode(request.paymentMode);
        payment.setTransactionId(request.transactionId);
        payment.setPaymentDate(
                request.paymentDate != null ? request.paymentDate : LocalDate.now()
        );

        paymentRepository.save(payment);
    }

    public Page<Payment> getPayments(
            String memberId,
            String month,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "paymentDate")
        );

        Specification<Payment> spec = (root, query, cb) -> cb.conjunction();
        if (memberId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("member").get("id"), memberId));
        }

        if (month != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("month"), month));
        }

        return paymentRepository.findAll(spec, pageable);
    }
}

