package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.WorkerEntity;
import com.rmit.sept.septbackend.model.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends CrudRepository<WorkerEntity, Integer> {
    boolean existsByUserUsername(String username);
    WorkerEntity getByWorkerId(int workerId);
    List<WorkerEntity> findAllByStatus(Status status);

}
