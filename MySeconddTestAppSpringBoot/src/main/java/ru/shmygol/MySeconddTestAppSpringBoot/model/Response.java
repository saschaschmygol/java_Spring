package ru.shmygol.MySeconddTestAppSpringBoot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    // Уникальный идентификатор сообщения из запроса. Обязательное поле ответа.
    private String uid;

    // Уникальный идентификатор операции из запроса. Обязательное поле ответа.
    private String operationUid;

    // Время создания сообщения ответа. Обязательное поле.
    private String systemTime;

    // Рассчитанная годовая премия. Заполняется при успешном расчёте.
    private Double annualBonus;

    // Результат обработки запроса: SUCCESS или FAILED. Обязательное поле.
    private Codes code;

    // Код ошибки. При отсутствии ошибки — EMPTY. Обязательное поле.
    private ErrorCodes errorCode;

    // Сообщение об ошибке. При отсутствии ошибки — EMPTY. Обязательное поле.
    private ErrorMessages errorMessage;
}
