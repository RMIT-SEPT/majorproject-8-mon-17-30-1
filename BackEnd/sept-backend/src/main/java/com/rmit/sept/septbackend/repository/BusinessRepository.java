package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.BusinessEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends CrudRepository<BusinessEntity, Integer> {

    BusinessEntity getByBusinessName(String businessName);

    boolean existsByBusinessName(String businessName);

}
