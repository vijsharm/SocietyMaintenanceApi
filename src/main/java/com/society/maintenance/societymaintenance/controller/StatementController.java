package com.society.maintenance.societymaintenance.controller;

import com.society.maintenance.societymaintenance.dto.MemberStatementResponse;
import com.society.maintenance.societymaintenance.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statements")
@RequiredArgsConstructor
public class StatementController {

    private final StatementService statementService;

    @GetMapping("/{memberId}")
    public MemberStatementResponse getStatement(
            @PathVariable Long memberId
    ) {
        return statementService.getMemberStatement(memberId);
    }
}
