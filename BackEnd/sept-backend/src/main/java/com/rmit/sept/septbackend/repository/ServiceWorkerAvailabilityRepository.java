package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.ServiceWorkerAvailabilityEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceWorkerAvailabilityRepository extends CrudRepository<ServiceWorkerAvailabilityEntity, Integer> {

    @Query("from ServiceWorkerAvailabilityEntity " +
            "where serviceWorker.serviceWorkerId = ?1 " +
            "and effectiveStartDate <= ?2 " +
            "and effectiveEndDate >= ?3 ")
    List<ServiceWorkerAvailabilityEntity> getAllByServiceWorkerId(int serviceWorkerId, LocalDate effectiveStartDate, LocalDate effectiveEndDate);

    @Query("from ServiceWorkerAvailabilityEntity " +
            "where serviceWorker.worker.workerId = ?1 " +
            "and ((effectiveStartDate <= ?2 and effectiveEndDate >= ?2) " +
            "or (effectiveStartDate <= ?3 and effectiveEndDate >= ?3) " +
            "or (effectiveStartDate >= ?2 and effectiveEndDate <= ?3)) ")
    List<ServiceWorkerAvailabilityEntity> getAllByWorkerId(int workerId, LocalDate effectiveStartDate, LocalDate effectiveEndDate);

    @Query("from ServiceWorkerAvailabilityEntity " +
            "where serviceWorker.service.serviceId = ?1 " +
            "and ((effectiveStartDate <= ?2 and effectiveEndDate >= ?2) " +
            "or (effectiveStartDate <= ?3 and effectiveEndDate >= ?3) " +
            "or (effectiveStartDate >= ?2 and effectiveEndDate <= ?3)) ")
    List<ServiceWorkerAvailabilityEntity> getAllByServiceId(int serviceId, LocalDate effectiveStartDate, LocalDate effectiveEndDate);

    @Query("from ServiceWorkerAvailabilityEntity " +
            "where serviceWorker.worker.workerId = ?1 " +
            "and serviceWorker.service.serviceId = ?2 " +
            "and ((effectiveStartDate <= ?3 and effectiveEndDate >= ?3) " +
            "or (effectiveStartDate <= ?4 and effectiveEndDate >= ?4) " +
            "or (effectiveStartDate >= ?3 and effectiveEndDate <= ?4)) ")
    List<ServiceWorkerAvailabilityEntity> getAllByWorkerIdServiceId(int workerId, int serviceId, LocalDate effectiveStartDate, LocalDate effectiveEndDate);
}
