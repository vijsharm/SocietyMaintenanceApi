package com.society.maintenance.societymaintenance.controller;

import com.society.maintenance.societymaintenance.dto.PendingDuesResponse;
import com.society.maintenance.societymaintenance.dto.ReportsResponseDTO;
import com.society.maintenance.societymaintenance.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/pending-dues")
    public List<PendingDuesResponse> getPendingDues() {
        return statisticsService.getPendingDuesForAllMembers();
    }

    @GetMapping("/reports")
    public ReportsResponseDTO getReports(
            @RequestParam(required = false) String startMonth,
            @RequestParam(required = false) String endMonth
    ) {
        return statisticsService.getReports(startMonth, endMonth);
    }
}
