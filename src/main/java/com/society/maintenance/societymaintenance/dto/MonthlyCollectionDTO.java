package com.society.maintenance.societymaintenance.dto;

import lombok.Data;

@Data
public class MonthlyCollectionDTO {
    private String month;
    private Double collected;
    private Double expected;
    private Double pending;
}
