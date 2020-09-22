package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BusinessEntity;
import com.rmit.sept.septbackend.model.BusinessResponse;
import com.rmit.sept.septbackend.repository.AdminRepository;
import com.rmit.sept.septbackend.repository.BusinessRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BusinessServiceTests {

    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminRepository adminRepository;
    private BusinessService businessService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        businessService = new BusinessService(businessRepository, userRepository, adminRepository);
    }

    @Test
    public void testGetAllBusinesses() {
        Mockito.when(businessRepository.findAll()).thenReturn(
                Arrays.asList(
                        new BusinessEntity(0, "testBusiness"),
                        new BusinessEntity(1, "anotherTestBusiness")
                )
        );

        List<BusinessResponse> expected = Arrays.asList(
                new BusinessResponse("testBusiness", 0),
                new BusinessResponse("anotherTestBusiness", 1)
        );

        List<BusinessResponse> actual = businessService.getAllBusinesses();

        Assertions.assertEquals(expected, actual);
    }
}
