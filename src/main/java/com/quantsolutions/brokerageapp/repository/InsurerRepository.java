package com.quantsolutions.brokerageapp.repository;


import com.quantsolutions.brokerageapp.model.Insurer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurerRepository extends JpaRepository<Insurer,Long> {
}
