package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.WorkerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends CrudRepository<WorkerEntity, Integer> {
    boolean existsByUserUsername(String username);
    WorkerEntity getByWorkerId(int workerId);

}
