package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.AdminEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<AdminEntity, Integer> {
    void getByUserUsername(String username);
}
