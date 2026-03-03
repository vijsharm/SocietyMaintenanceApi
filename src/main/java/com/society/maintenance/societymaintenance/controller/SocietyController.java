package com.society.maintenance.societymaintenance.controller;

import com.society.maintenance.societymaintenance.dto.SocietyBalanceDto;
import com.society.maintenance.societymaintenance.service.SocietyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocietyController {

    private final SocietyService societyService;

    @GetMapping("/society/balance")
    public SocietyBalanceDto getBalance() {
        return societyService.getSocietyBalance();
    }

    @PutMapping("/society-balance")
    public Map<String, Object> updateBalance(
            @RequestBody SocietyBalanceDto request) {

        String message = societyService
                .updateInitialBalance(request.getInitialBalance());

        return Map.of(
                "success", true,
                "message", message
        );
    }
}
