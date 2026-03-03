package com.society.maintenance.societymaintenance.controller;

import com.society.maintenance.societymaintenance.dto.ApiResponse;
import com.society.maintenance.societymaintenance.dto.ElectricityDueBatchRequest;
import com.society.maintenance.societymaintenance.service.ElectricityDueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/electricity-dues")
@RequiredArgsConstructor
public class ElectricityDueController {

    private final ElectricityDueService service;

    @PutMapping("/batch")
    public ApiResponse<Integer> updateElectricityDuesBatch(
            @RequestBody ElectricityDueBatchRequest request
    ) {
        int updatedCount = service.updateElectricityDuesBatch(request);
        return ApiResponse.success(
                "Electricity arrears updated successfully",
                updatedCount
        );
    }
}
