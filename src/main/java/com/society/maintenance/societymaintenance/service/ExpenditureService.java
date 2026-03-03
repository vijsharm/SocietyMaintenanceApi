package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.ExpenditureDto;
import com.society.maintenance.societymaintenance.entity.Expenditure;
import com.society.maintenance.societymaintenance.repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private final ExpenditureRepository repository;

    public Expenditure addExpenditure(ExpenditureDto request) {

        Expenditure expenditure = new Expenditure();
        expenditure.setAmount(request.amount);
        expenditure.setCategory(request.category);
        expenditure.setDescription(request.description);
        //expenditure.setAddedBy(request.addedBy);
        expenditure.setDate(
                request.date != null ? request.date : LocalDate.now()
        );

        return repository.save(expenditure);
    }

    public List<ExpenditureDto> getAllExpenditures() {

        return repository.findAll()
                .stream()
                .map(exp -> {
                    ExpenditureDto response = new ExpenditureDto();
                    response.setId(String.valueOf(exp.getId()));
                    response.setAmount(exp.getAmount());
                    response.setCategory(exp.getCategory());
                    response.setDescription(exp.getDescription());
                    response.setDate(exp.getDate());
                    //response.setAddedBy(exp.getAddedBy());
                    return response;
                })
                .toList();
    }
}

