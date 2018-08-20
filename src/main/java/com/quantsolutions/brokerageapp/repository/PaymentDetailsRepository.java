package com.quantsolutions.brokerageapp.repository;


import com.quantsolutions.brokerageapp.model.PaymentDetails;
import com.quantsolutions.brokerageapp.model.Policy;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {


}
