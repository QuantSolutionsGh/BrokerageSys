package com.quantsolutions.brokerageapp.repository;


import com.quantsolutions.brokerageapp.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoles,Long> {
}
