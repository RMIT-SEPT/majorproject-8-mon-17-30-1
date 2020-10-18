package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.model.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceWorkerRepository extends CrudRepository<ServiceWorkerEntity, Integer> {

    List<ServiceWorkerEntity> getAllByWorkerUserUsernameAndServiceStatusAndWorkerStatus(String username, Status serviceStatus, Status workerStatus);

    List<ServiceWorkerEntity> getAllByServiceServiceIdAndServiceStatusAndWorkerStatus(int serviceId, Status serviceStatus, Status workerStatus);

    ServiceWorkerEntity getByServiceServiceIdAndWorkerWorkerId(int serviceId, int workerId);

    List<ServiceWorkerEntity> getAllByWorkerWorkerId(int workerId);

}
