package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.AdminEntity;
import com.rmit.sept.septbackend.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Integer> {
    CustomerEntity getByUserUsername(String username);
    boolean existsByUserUsername(String username);
}
