package com.quantsolutions.brokerageapp.repository;


import com.quantsolutions.brokerageapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {



    Users findUsersByUsername(String username);
}
