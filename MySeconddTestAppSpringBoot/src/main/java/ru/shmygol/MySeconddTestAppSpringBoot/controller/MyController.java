package ru.shmygol.MySeconddTestAppSpringBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import ru.shmygol.MySeconddTestAppSpringBoot.exception.ValidationFailedException;
import ru.shmygol.MySeconddTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.shmygol.MySeconddTestAppSpringBoot.model.*;
import ru.shmygol.MySeconddTestAppSpringBoot.service.ValidationService;
import ru.shmygol.MySeconddTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;

    @Autowired
    public MyController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(
            @Valid @RequestBody Request request,
            BindingResult bindingResult) {

        log.info("Получен request: {}", request);
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Создан response: {}", response);

        try {
            validationService.isValid(bindingResult);
            log.info("Проверка request завершена успешно: uid={}", request.getUid());
            if ("123".equals(request.getUid())) {
                log.error("Не поддерживаемый uid в request: {}", request.getUid());
                throw new UnsupportedCodeException("Не поддерживаемая ошибка");
            }
        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации request: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Изменён response после ошибки валидации: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Не поддерживаемый запрос: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Изменён response после ошибки: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при обработке request", e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Изменён response после непредвиденной ошибки: {}", response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Возвращён успешный response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
