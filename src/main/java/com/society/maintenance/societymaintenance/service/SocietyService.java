package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.Role;
import com.society.maintenance.societymaintenance.dto.SocietyBalanceDto;
import com.society.maintenance.societymaintenance.entity.Society;
import com.society.maintenance.societymaintenance.entity.User;
import com.society.maintenance.societymaintenance.repository.ExpenditureRepository;
import com.society.maintenance.societymaintenance.repository.PaymentRepository;
import com.society.maintenance.societymaintenance.repository.SocietyRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocietyService {

    private final SocietyRepository societyRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenditureRepository expenditureRepository;

    public SocietyBalanceDto getSocietyBalance() {

        Society society = societyRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Society not initialized"));

        Double totalPayments = paymentRepository.getTotalPayments();
        Double totalExpenditure = expenditureRepository.getTotalExpenditure();

        Double currentBalance = society.getInitialBalance()
                + totalPayments
                - totalExpenditure;

        SocietyBalanceDto response = new SocietyBalanceDto();
        response.setInitialBalance(society.getInitialBalance());
        response.setTotalPayments(totalPayments);
        response.setTotalExpenditure(totalExpenditure);
        response.setCurrentBalance(currentBalance);

        return response;
    }

    @Transactional
    public String updateInitialBalance(Double initialBalance) {

        if (initialBalance == null || initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance must be non-negative");
        }

        Society society = societyRepository.findById(1L)
                .orElseGet(() -> {
                    Society newSociety = new Society();
                    newSociety.setInitialBalance(0.0);
                    return newSociety;
                });

        society.setInitialBalance(initialBalance);

        societyRepository.save(society);

        return "Initial balance updated successfully";
    }

    @PostConstruct
    void loadUsers() {
        if (societyRepository.count() == 0) {

            Society society = new Society();
            society.setInitialBalance(1000.0);

            societyRepository.save(society);
        }
    }
}