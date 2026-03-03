package com.society.maintenance.societymaintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PendingDues {

    private List<MaintenanceDue> maintenanceDues;
    private double electricityArrear;
    private double totalPending;
}
