package ru.shmygol.MySeconddTestAppSpringBoot;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import ru.shmygol.MySeconddTestAppSpringBoot.controller.MyController;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Request;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Response;
import ru.shmygol.MySeconddTestAppSpringBoot.service.RequestValidationService;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .systemName("ERP")
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
        assertEquals("failed", response.getCode());
        assertEquals("UnsupportedCodeException", response.getErrorCode());
        assertEquals("Не поддерживаемая ошибка", response.getErrorMessage());
    }
}
