package com.quantsolutions.brokerageapp.repository;


import com.quantsolutions.brokerageapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
