package com.quantsolutions.brokerageapp.repository;

import com.quantsolutions.brokerageapp.model.PaymentDetails;
import com.quantsolutions.brokerageapp.model.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy,Long> {

    @Query("select t from PaymentDetails t where t.policy.id = :id")
    Page<PaymentDetails> findByPolicyId(@Param("id") long policyId, Pageable pageRequest);

}
