package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.DashboardResponse;
import com.society.maintenance.societymaintenance.entity.Member;
import com.society.maintenance.societymaintenance.entity.Society;
import com.society.maintenance.societymaintenance.repository.ExpenditureRepository;
import com.society.maintenance.societymaintenance.repository.MemberRepository;
import com.society.maintenance.societymaintenance.repository.PaymentRepository;
import com.society.maintenance.societymaintenance.repository.SocietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenditureRepository expenditureRepository;
    private final SocietyRepository societyRepository;

    public DashboardResponse getDashboardStats() {

        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        Long totalMembers = memberRepository.getTotalMembers();
        Double expectedPerMonth = memberRepository.getTotalExpectedPerMonth();

        Double collectedThisMonth =
                paymentRepository.getCollectedThisMonth(month, year);

        Long paymentsThisMonth =
                paymentRepository.getPaymentsCountThisMonth(month, year);

        Double totalPayments = paymentRepository.getTotalPayments();
        Double totalExpenditure = expenditureRepository.getTotalExpenditure();

        Society society = societyRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Society not initialized"));

        Double societyBalance =
                society.getInitialBalance()
                        + totalPayments
                        - totalExpenditure;

        Double pendingThisMonth = expectedPerMonth - collectedThisMonth;

        Double totalExpectedAllMonths = calculateTotalExpectedAllMonths();
        Double totalPendingAllMonths = totalExpectedAllMonths - totalPayments;

        return DashboardResponse.builder()
                .totalMembers(totalMembers)
                .collectedThisMonth(collectedThisMonth)
                .expectedThisMonth(expectedPerMonth)
                .pendingThisMonth(pendingThisMonth)
                .paymentsThisMonth(paymentsThisMonth)
                .societyBalance(societyBalance)
                .totalExpenditure(totalExpenditure)
                .totalPendingAllMonths(totalPendingAllMonths)
                .build();
    }

    public Double calculateTotalExpectedAllMonths() {

        LocalDate now = LocalDate.now();

        return memberRepository.findAll()
                .stream()
                .filter(m -> m.getActiveSince() != null)
                .mapToDouble(member -> {
                    long months = ChronoUnit.MONTHS.between(
                            member.getActiveSince().withDayOfMonth(1),
                            now.withDayOfMonth(1)
                    ) + 1;

                    if (months < 0) months = 0;

                    return member.getMonthlyAmount() * months;
                })
                .sum();
    }
}