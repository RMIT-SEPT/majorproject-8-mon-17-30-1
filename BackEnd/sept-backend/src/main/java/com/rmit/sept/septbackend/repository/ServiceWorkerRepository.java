package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceWorkerRepository extends CrudRepository<ServiceWorkerEntity, Integer> {

    List<ServiceWorkerEntity> getAllByWorkerUserUsername(String username);

}
