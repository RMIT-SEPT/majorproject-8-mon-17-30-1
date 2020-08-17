package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.UserEntity;
import com.rmit.sept.septbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserById(Integer userId) {
        // Complex stuff here

        return userRepository.getByUserId(userId);
    }

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
