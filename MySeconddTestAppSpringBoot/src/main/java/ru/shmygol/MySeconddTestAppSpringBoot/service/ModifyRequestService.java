package ru.shmygol.MySeconddTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Request;

@Service
public interface ModifyRequestService {

    void modify(Request request);
}
