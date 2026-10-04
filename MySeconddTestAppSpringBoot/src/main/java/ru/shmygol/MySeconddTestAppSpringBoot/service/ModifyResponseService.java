package ru.shmygol.MySeconddTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Response;

@Service
public interface ModifyResponseService {

    Response modify(Response response);
}
