package ru.shmygol.MySeconddTestAppSpringBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.shmygol.MySeconddTestAppSpringBoot.service.AnnualBonusService;
import ru.shmygol.MySeconddTestAppSpringBoot.service.ModifyRequestService;
import ru.shmygol.MySeconddTestAppSpringBoot.service.ModifyResponseService;
import ru.shmygol.MySeconddTestAppSpringBoot.service.ValidationService;
import ru.shmygol.MySeconddTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final ModifyRequestService modifySourceRequestService;
    private final AnnualBonusService annualBonusService;

    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        @Qualifier("ModifySystemNameRequestService") ModifyRequestService modifySystemNameRequestService,
                        @Qualifier("ModifySourceRequestService") ModifyRequestService modifySourceRequestService,
                        AnnualBonusService annualBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifySourceRequestService = modifySourceRequestService;
        this.annualBonusService = annualBonusService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(
            @Valid @RequestBody Request request,
            BindingResult bindingResult) {

        request.setService1ReceivedAtMillis(System.currentTimeMillis());
        log.info("Получен request: {}", request);
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        try {
            validationService.isValid(bindingResult);
            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("Не поддерживаемая ошибка");
            }
            response.setAnnualBonus(annualBonusService.calculate(
                    request.getPosition(),
                    request.getSalary(),
                    request.getBonus(),
                    request.getWorkDays()));
        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации request: {}", e.getMessage());
            return errorResponse(response, ErrorCodes.VALIDATION_EXCEPTION,
                    ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Не поддерживаемый запрос: {}", e.getMessage());
            return errorResponse(response, ErrorCodes.UNSUPPORTED_EXCEPTION,
                    ErrorMessages.UNSUPPORTED, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при обработке request", e);
            return errorResponse(response, ErrorCodes.UNKNOWN_EXCEPTION,
                    ErrorMessages.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
        }

//        modifyResponseService.modify(response);
//        modifySystemNameRequestService.modify(request);
//        modifySourceRequestService.modify(request);

        log.info("Возвращён успешный response: {}", response);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Response> errorResponse(Response response, ErrorCodes errorCode,
                                                   ErrorMessages errorMessage, HttpStatus status) {
        response.setCode(Codes.FAILED);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return ResponseEntity.status(status).body(response);
    }
}
