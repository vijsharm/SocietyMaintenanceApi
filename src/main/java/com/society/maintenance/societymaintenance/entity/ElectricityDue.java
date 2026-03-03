package com.society.maintenance.societymaintenance.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "electricity_dues", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id"})
})
@Data
public class ElectricityDue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 FK → Member
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "electricity_arrear", nullable = false)
    private Double electricityArrear;

}
