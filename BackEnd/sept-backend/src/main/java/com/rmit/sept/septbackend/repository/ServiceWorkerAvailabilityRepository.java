package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.ServiceWorkerAvailabilityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceWorkerAvailabilityRepository extends CrudRepository<ServiceWorkerAvailabilityEntity, Integer> {

    List<ServiceWorkerAvailabilityEntity> getAllByServiceWorkerServiceWorkerIdAndEffectiveStartDateBeforeAndEffectiveEndDateAfter(int serviceWorkerId, LocalDate effectiveStartDate, LocalDate effectiveEndDate);

}
