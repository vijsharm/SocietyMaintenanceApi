package com.society.maintenance.societymaintenance.dto;

import lombok.Data;

@Data
public class SocietyBalanceDto {

    private Double initialBalance;
    private Double totalPayments;
    private Double totalExpenditure;
    private Double currentBalance;
}
