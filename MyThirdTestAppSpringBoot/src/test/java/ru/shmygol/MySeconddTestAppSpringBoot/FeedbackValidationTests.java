package ru.shmygol.MySeconddTestAppSpringBoot;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import ru.shmygol.MySeconddTestAppSpringBoot.controller.MyController;
import ru.shmygol.MySeconddTestAppSpringBoot.exception.ValidationFailedException;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Request;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Response;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Codes;
import ru.shmygol.MySeconddTestAppSpringBoot.model.ErrorCodes;
import ru.shmygol.MySeconddTestAppSpringBoot.model.ErrorMessages;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Systems;
import ru.shmygol.MySeconddTestAppSpringBoot.service.RequestValidationService;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FeedbackValidationTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final MyController controller = new MyController(new RequestValidationService());

    @Test
    void requiredRequestFieldsAreValidated() {
        Request request = new Request();
        Set<String> invalidFields = validator.validate(request).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertEquals(Set.of("uid", "operationUid", "systemTime"), invalidFields);
    }

    @Test
    void uid123ReturnsUnsupportedCodeException() {
        Request request = Request.builder()
                .uid("123")
                .operationUid("operation-1")
                .systemName(Systems.ERP)
                .systemTime("2026-09-27T12:00:00Z")
                .source("test")
                .communicationId(1)
                .templateId(1)
                .productCode(1)
                .smsCode(1)
                .build();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(request, "request");

        var result = controller.feedback(request, bindingResult);
        Response response = result.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(Codes.FAILED, response.getCode());
        assertEquals(ErrorCodes.UNSUPPORTED_EXCEPTION, response.getErrorCode());
        assertEquals(ErrorMessages.UNSUPPORTED, response.getErrorMessage());
    }

    @Test
    void validationExceptionContainsAllBindingMessages() {
        Request request = new Request();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        bindingResult.addError(new FieldError("request", "uid", "uid is required"));
        bindingResult.addError(new FieldError("request", "systemTime", "systemTime is required"));

        ValidationFailedException exception = assertThrows(ValidationFailedException.class,
                () -> new RequestValidationService().isValid(bindingResult));

        assertEquals("uid: uid is required; systemTime: systemTime is required", exception.getMessage());
    }
}
