package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.User;
import com.rmit.sept.septbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Integer userId) {
        return userRepository.getByUserId(userId);
    }

}
