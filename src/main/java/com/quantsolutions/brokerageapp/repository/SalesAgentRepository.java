package com.quantsolutions.brokerageapp.repository;


import com.quantsolutions.brokerageapp.model.SalesAgent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesAgentRepository extends JpaRepository<SalesAgent,Long> {
}
