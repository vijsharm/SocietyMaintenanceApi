package com.society.maintenance.societymaintenance.controller;

import com.society.maintenance.societymaintenance.dto.PendingDuesResponse;
import com.society.maintenance.societymaintenance.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
