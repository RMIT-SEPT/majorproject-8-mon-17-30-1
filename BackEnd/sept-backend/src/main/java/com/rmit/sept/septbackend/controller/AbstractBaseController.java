package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.exception.ResponseException;
import com.rmit.sept.septbackend.model.ValidationResponse;

public class AbstractBaseController implements BaseController {
    protected <T> T handleValidationResponse(ValidationResponse<T> validationResponse) {
        if (!validationResponse.isSuccessful()) {
            throw new ResponseException(validationResponse.getErrors());
        }
        return validationResponse.getBody();
    }
}
