package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.ServiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends CrudRepository<ServiceEntity, Integer> {

    List<ServiceEntity> getAllByBusinessBusinessName(String businessName);

}