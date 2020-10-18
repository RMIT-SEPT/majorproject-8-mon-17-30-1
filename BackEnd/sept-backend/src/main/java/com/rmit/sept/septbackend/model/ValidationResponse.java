package com.rmit.sept.septbackend.model;

import com.rmit.sept.septbackend.exception.Error;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ValidationResponse<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected List<Error> errors = new ArrayList<>();
    protected T body;

    public ValidationResponse(T body) {
        this.body = body;
    }

    public boolean isSuccessful() {
        return errors.isEmpty();
    }

    public void addError(Error error) {
        this.errors.add(error);
    }

    public void addError(String message) {
        this.addError(message, message);
    }

    public void addError(String message, String debugMessage, Object... debugArgs) {
        Error error = new Error(message, String.format(debugMessage, debugArgs));
        this.errors.add(error);
        logger.warn("Service error occurred: {}", error.getDebugMessage());
    }
}
