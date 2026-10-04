package ru.shmygol.MySeconddTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.shmygol.MySeconddTestAppSpringBoot.exception.ValidationFailedException;

import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestValidationService implements ValidationService {

    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream()
                    .map(error -> {
                        String message = error.getDefaultMessage() == null ? error.toString() : error.getDefaultMessage();
                        return error instanceof FieldError fieldError
                                ? fieldError.getField() + ": " + message
                                : message;
                    })
                    .collect(Collectors.joining("; "));
            log.error("Ошибки BindingResult: {}", errors);
            throw new ValidationFailedException(errors);
        }
    }
}
