package com.society.maintenance.societymaintenance.repository;

import com.society.maintenance.societymaintenance.entity.ElectricityDue;
import com.society.maintenance.societymaintenance.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectricityDueRepository extends JpaRepository<ElectricityDue, Long> {

    Optional<ElectricityDue> findByMember(Member member);
}
