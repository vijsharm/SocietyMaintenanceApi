package com.society.maintenance.societymaintenance.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"flat_number"})
})
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String name;

    @Column(name = "flat_number", nullable = false)
    private String flatNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name = "monthly_amount", nullable = false)
    private Double monthlyAmount;

    @Column(name = "active_since", nullable = false)
    private LocalDate activeSince;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "member")
    private List<Payment> payments;

}

