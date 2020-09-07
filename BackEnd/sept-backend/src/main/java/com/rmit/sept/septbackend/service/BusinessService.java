package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.model.BusinessResponse;
import com.rmit.sept.septbackend.repository.BusinessRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class BusinessService {

    private final BusinessRepository businessRepository;

    public List<BusinessResponse> getAllBusinesses() {
        return StreamSupport
                .stream(businessRepository.findAll().spliterator(), false)
                .map(businessEntity -> new BusinessResponse(businessEntity.getBusinessName()))
                .collect(Collectors.toList());
    }
}
