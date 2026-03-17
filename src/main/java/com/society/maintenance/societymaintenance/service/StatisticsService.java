package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.*;
import com.society.maintenance.societymaintenance.entity.Member;
import com.society.maintenance.societymaintenance.entity.Payment;
import com.society.maintenance.societymaintenance.repository.ElectricityDueRepository;
import com.society.maintenance.societymaintenance.repository.MemberRepository;
import com.society.maintenance.societymaintenance.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final ElectricityDueRepository electricityDueRepository;

    public List<PendingDuesResponse> getPendingDuesForAllMembers() {

        List<Member> members = memberRepository.findAll();
        YearMonth currentMonth = YearMonth.now();

        return members.stream()
                .map(member -> calculatePendingForMember(member, currentMonth))
                .toList();
    }

    private PendingDuesResponse calculatePendingForMember(
            Member member,
            YearMonth currentMonth
    ) {
        YearMonth start = YearMonth.from(member.getActiveSince());
        List<PendingMonthDTO> pendingMonths = new ArrayList<>();
        double totalMaintenancePending = 0.0;
        List<Payment> payments =
                paymentRepository.findByMemberId(member.getId());

        double monthlyAmount = member.getMonthlyAmount();


        while (!start.isAfter(currentMonth)) {

            String monthStr = start.toString();

            boolean paid = payments.stream()
                    .anyMatch(p -> monthStr.equals(p.getMonth().toString().trim()));

            if (!paid) {
                pendingMonths.add(
                        new PendingMonthDTO(monthStr, monthlyAmount)
                );
                totalMaintenancePending += monthlyAmount;
            }

            start = start.plusMonths(1);
        }

        double electricityArrear = electricityDueRepository
                .findByMember(member)
                .map(e -> e.getElectricityArrear() == null ? 0.0 : e.getElectricityArrear())
                .orElse(0.0);

        double totalPending = totalMaintenancePending + electricityArrear;

        return new PendingDuesResponse(
                member.getId(),
                member.getName(),
                member.getFlatNumber(),
                pendingMonths,
                electricityArrear,
                totalPending
        );
    }

    public ReportsResponseDTO getReports(String startMonth, String endMonth) {

        YearMonth start = startMonth != null
                ? YearMonth.parse(startMonth)
                : YearMonth.now().minusMonths(5);

        YearMonth end = endMonth != null
                ? YearMonth.parse(endMonth)
                : YearMonth.now();

        List<Member> members = memberRepository.findAll();
        List<Payment> payments = paymentRepository.findAll();

        Map<YearMonth, List<Payment>> paymentsByMonth = payments.stream()
                .filter(p -> p.getMonth() != null)
                .collect(Collectors.groupingBy(Payment::getMonth));

        List<MonthlyCollectionDTO> monthlyList = new ArrayList<>();

        YearMonth cursor = start;

        while (!cursor.isAfter(end)) {

            List<Payment> monthPayments =
                    paymentsByMonth.getOrDefault(cursor, List.of());

            double collected = monthPayments.stream()
                    .mapToDouble(Payment::getAmount)
                    .sum();

            double expected = members.stream()
                    .mapToDouble(Member::getMonthlyAmount)
                    .sum();

            double pending = expected - collected;

            MonthlyCollectionDTO dto = new MonthlyCollectionDTO();
            dto.setMonth(cursor.toString());
            dto.setCollected(collected);
            dto.setExpected(expected);
            dto.setPending(pending);

            monthlyList.add(dto);

            cursor = cursor.plusMonths(1);
        }

        // ================= MEMBER STATUS =================

        List<MemberPaymentStatusDTO> memberStatusList = new ArrayList<>();

        for (Member member : members) {

            List<Payment> memberPayments =
                    payments.stream()
                            .filter(p -> p.getMember().getId().equals(member.getId()))
                            .toList();

            int paidMonths = (int) memberPayments.stream()
                    .map(p -> p.getMonth())
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();

            YearMonth activeSince = YearMonth.from(member.getActiveSince());
            YearMonth now = YearMonth.now();

            long totalMonths = ChronoUnit.MONTHS.between(activeSince, now) + 1;

            int pendingMonths = (int) (totalMonths - paidMonths);
            if (pendingMonths < 0) pendingMonths = 0;

            double totalPaid = memberPayments.stream()
                    .mapToDouble(Payment::getAmount)
                    .sum();

            double totalExpected = totalMonths * member.getMonthlyAmount();

            double totalPending = totalExpected - totalPaid;

            MemberPaymentStatusDTO dto = new MemberPaymentStatusDTO();
            dto.setMemberId(member.getId());
            dto.setMemberName(member.getName());
            dto.setFlatNumber(member.getFlatNumber());
            dto.setPaidMonths(paidMonths);
            dto.setPendingMonths(pendingMonths);
            dto.setTotalPaid(totalPaid);
            dto.setTotalPending(totalPending);

            memberStatusList.add(dto);
        }

        ReportsResponseDTO response = new ReportsResponseDTO();
        response.setMonthlyCollection(monthlyList);
        response.setMemberPaymentStatus(memberStatusList);

        return response;
    }
}
