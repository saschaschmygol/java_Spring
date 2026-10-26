package ru.shmygol.MySeconddTestAppSpringBoot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    // Уникальный идентификатор сообщения. Обязательное поле, до 32 символов.
    @NotBlank(message="UID не может быть пустым")
    @Size(max = 32)
    private String uid;

    // Уникальный идентификатор операции. Обязательное поле, до 32 символов.
    @NotBlank
    @Size(max = 32)
    private String operationUid;

    // Имя системы отправителя. Необязательное поле.
    private Systems systemName;

    // Время создания сообщения. Обязательное поле.
    @NotBlank
    private String systemTime;

    // Наименование ресурса. Необязательное поле.
    private String source;

    // Должность сотрудника для расчёта премии.
    private Positions position;

    // Зарплата сотрудника для расчёта премии.
    private Double salary;

    // Коэффициент премии.
    private Double bonus;

    // Количество отработанных дней для расчёта премии.
    private Integer workDays;

    // Уникальный идентификатор коммуникации. Необязательное поле, от 1 до 100000.
    @Min(1)
    @Max(100000)
    private Integer communicationId;

    // Уникальный идентификатор шаблона. Необязательное поле.
    private Integer templateId;

    // Код продукта. Необязательное поле.
    private Integer productCode;

    // СМС-код. Необязательное поле.
    private Integer smsCode;

    // Время получения запроса первым сервисом в миллисекундах Unix-времени. Заполняется сервером.
    private Long service1ReceivedAtMillis;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}
