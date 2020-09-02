package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.AdminEntity;
import com.rmit.sept.septbackend.entity.WorkerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends CrudRepository<WorkerEntity, Integer> {
    void getByUserUsername(String username);
}
