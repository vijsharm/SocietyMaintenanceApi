package com.society.maintenance.societymaintenance.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenditureDto {

    public String id;
    public Double amount;
    public String category;
    public String description;
    public LocalDate date;
    public String addedBy;
}
