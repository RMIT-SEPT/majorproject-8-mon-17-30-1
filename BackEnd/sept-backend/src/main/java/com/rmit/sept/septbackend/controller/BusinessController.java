package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.BusinessResponse;
import com.rmit.sept.septbackend.service.BusinessService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/business")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping
    public List<BusinessResponse> getAllBusinesses() {
        return businessService.getAllBusinesses();
    }
}
