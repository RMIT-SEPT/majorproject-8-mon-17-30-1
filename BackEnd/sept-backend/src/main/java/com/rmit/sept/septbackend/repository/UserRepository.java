package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User getByUserId(Integer userId);

}
