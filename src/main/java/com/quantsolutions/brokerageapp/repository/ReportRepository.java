package com.quantsolutions.brokerageapp.repository;


import com.quantsolutions.brokerageapp.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {


}
