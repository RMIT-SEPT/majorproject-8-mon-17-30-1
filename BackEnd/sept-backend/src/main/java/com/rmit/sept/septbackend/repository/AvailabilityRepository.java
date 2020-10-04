package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.AvailabilityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends CrudRepository<AvailabilityEntity, Integer> {
}
