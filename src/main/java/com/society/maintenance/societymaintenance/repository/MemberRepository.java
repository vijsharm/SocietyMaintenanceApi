package com.society.maintenance.societymaintenance.repository;

import com.society.maintenance.societymaintenance.dto.MemberDto;
import com.society.maintenance.societymaintenance.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByFlatNumber(String flatNumber);

    @Query("""
        SELECT new com.society.maintenance.societymaintenance.dto.MemberDto(
            m.id,
            m.name,
            m.flatNumber,
            m.email,
            m.phone,
            m.monthlyAmount,
            d.electricityArrear,
            m.activeSince
        )
        FROM Member m
        LEFT JOIN ElectricityDue d
            ON m.id = d.member.id
    """)
    List<MemberDto> findAllWithElectricityDues();

    @Query("SELECT COUNT(m) FROM Member m")
    Long getTotalMembers();

    @Query("SELECT COALESCE(SUM(m.monthlyAmount),0) FROM Member m")
    Double getTotalExpectedPerMonth();
}

