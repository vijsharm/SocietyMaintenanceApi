package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.PendingDuesResponse;
import com.society.maintenance.societymaintenance.dto.PendingMonthDTO;
import com.society.maintenance.societymaintenance.entity.Member;
import com.society.maintenance.societymaintenance.entity.Payment;
import com.society.maintenance.societymaintenance.repository.ElectricityDueRepository;
import com.society.maintenance.societymaintenance.repository.MemberRepository;
import com.society.maintenance.societymaintenance.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

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
}
