package ru.shmygol.MySeconddTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Response;
import ru.shmygol.MySeconddTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@Service
@Qualifier("ModifySystemTimeResponseService")
public class ModifySystemTimeResponseService
        implements ModifyResponseService {

    @Override
    public Response modify(Response response) {

        String previousSystemTime = response.getSystemTime();
        response.setSystemTime(
                DateTimeUtil.getCustomFormat()
                        .format(new Date())
        );
        log.info("Изменён response.systemTime: {} -> {}", previousSystemTime, response.getSystemTime());

        return response;
    }
}
