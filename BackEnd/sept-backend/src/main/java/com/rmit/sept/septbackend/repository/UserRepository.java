package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    UserEntity getByUserId(Integer userId);

    UserEntity getByUsername(String username);

}
