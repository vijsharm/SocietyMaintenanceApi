package com.society.maintenance.societymaintenance.repository;


import com.society.maintenance.societymaintenance.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expenditure e")
    Double getTotalExpenditure();
}

