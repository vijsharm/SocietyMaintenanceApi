package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.*;
import com.society.maintenance.societymaintenance.entity.Member;
import com.society.maintenance.societymaintenance.entity.Payment;
import com.society.maintenance.societymaintenance.repository.ElectricityDueRepository;
import com.society.maintenance.societymaintenance.repository.MemberRepository;
import com.society.maintenance.societymaintenance.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;

    private final ElectricityDueRepository electricityDueRepository;

    public MemberStatementResponse getMemberStatement(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<Payment> payments =
                paymentRepository.findByMemberIdOrderByPaymentDateDesc(memberId);

        // ---- Calculate Maintenance Pending ----
        LocalDate now = LocalDate.now();
        LocalDate start = member.getActiveSince().withDayOfMonth(1);
        LocalDate currentMonth = now.withDayOfMonth(1);

        List<MaintenanceDue> maintenanceDues = new ArrayList<>();

        BigDecimal monthlyAmount =
                BigDecimal.valueOf(member.getMonthlyAmount());

        BigDecimal totalMaintenancePending = BigDecimal.ZERO;

        while (!start.isAfter(currentMonth)) {

            String monthStr = start.toString().substring(0, 7);

            boolean paid = payments.stream()
                    .anyMatch(p -> monthStr.equals(p.getMonth()));

            if (!paid) {
                maintenanceDues.add(
                        new MaintenanceDue(monthStr, monthlyAmount.doubleValue())
                );
                totalMaintenancePending =
                        totalMaintenancePending.add(monthlyAmount);
            }

            start = start.plusMonths(1);
        }

        double electricityArrear = electricityDueRepository.findByMember(member)
                .map(e -> e.getElectricityArrear() == null ? 0.0 : e.getElectricityArrear())
                .orElse(0.0);

        double totalPending =
                totalMaintenancePending.doubleValue() + electricityArrear;

        return mapToResponse(member, payments,
                maintenanceDues,
                electricityArrear,
                totalPending);
    }

    private MemberStatementResponse mapToResponse(
            Member member,
            List<Payment> payments,
            List<MaintenanceDue> maintenanceDues,
            double electricityArrear,
            double totalPending
    ) {

        MemberDto memberDTO = new MemberDto(
                member.getId(),
                member.getName(),
                member.getFlatNumber(),
                member.getEmail(),
                member.getPhone(),
                member.getMonthlyAmount(),
                electricityArrear,
                member.getActiveSince()
        );

        List<PaymentRequest> paymentDTOS = payments.stream()
                .map(p -> new PaymentRequest(
                        p.getId(),
                        member.getId(),
                        p.getAmount(),
                        p.getMonth(),
                        p.getPaymentDate(),
                        p.getPaymentMode(),
                        p.getTransactionId()
                ))
                .toList();

        PendingDues pending = new PendingDues(
                maintenanceDues,
                electricityArrear,
                totalPending
        );

        return new MemberStatementResponse(
                memberDTO,
                paymentDTOS,
                pending
        );
    }
}
