package com.society.maintenance.societymaintenance.controller;

import com.society.maintenance.societymaintenance.dto.ApiResponse;
import com.society.maintenance.societymaintenance.dto.ExpenditureDto;
import com.society.maintenance.societymaintenance.entity.Expenditure;
import com.society.maintenance.societymaintenance.service.ExpenditureService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureService service;

    public ExpenditureController(ExpenditureService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<Expenditure> addExpenditure(
            @RequestBody ExpenditureDto request
    ) {
        Expenditure expenditure = service.addExpenditure(request);
        return ApiResponse.success("Expenditure added successfully", expenditure);
    }

    @GetMapping
    public List<ExpenditureDto> getExpenditures() {
        return service.getAllExpenditures();
    }
}

