package com.society.maintenance.societymaintenance.repository;

import com.society.maintenance.societymaintenance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p")
    Double getTotalPayments();

    @Query("""
        SELECT COALESCE(SUM(p.amount),0)
        FROM Payment p
        WHERE MONTH(p.paymentDate) = :month
        AND YEAR(p.paymentDate) = :year
    """)
    Double getCollectedThisMonth(int month, int year);

    @Query("""
        SELECT COUNT(p)
        FROM Payment p
        WHERE MONTH(p.paymentDate) = :month
        AND YEAR(p.paymentDate) = :year
    """)
    Long getPaymentsCountThisMonth(int month, int year);

    List<Payment> findByMemberIdOrderByPaymentDateDesc(Long memberId);

    List<Payment> findByMemberId(Long memberId);

    List<Payment> findByPaymentDateBetween(LocalDate start, LocalDate end);

}

