package com.society.maintenance.societymaintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingMonthDTO {
    private String month;
    private double amount;
}