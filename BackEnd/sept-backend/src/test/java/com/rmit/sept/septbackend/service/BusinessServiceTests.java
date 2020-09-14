package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BusinessEntity;
import com.rmit.sept.septbackend.model.BusinessResponse;
import com.rmit.sept.septbackend.repository.BusinessRepository;
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

    private BusinessService businessService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        businessService = new BusinessService(businessRepository);
    }

    @Test
    public void testGetAllBusinesses() {
        Mockito.when(businessRepository.findAll()).thenReturn(
                Arrays.asList(
                        new BusinessEntity("testBusiness"),
                        new BusinessEntity("anotherTestBusiness")
                )
        );

        List<BusinessResponse> expected = Arrays.asList(
                new BusinessResponse("testBusiness"),
                new BusinessResponse("anotherTestBusiness")
        );

        List<BusinessResponse> actual = businessService.getAllBusinesses();

        Assertions.assertEquals(expected, actual);
    }
}
