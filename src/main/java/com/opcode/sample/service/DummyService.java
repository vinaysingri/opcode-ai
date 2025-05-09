package com.opcode.sample.service;

import com.opcode.sample.model.DummyResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class DummyService {

    public DummyResponse getDummyResponse() {
        return DummyResponse.builder()
                .message("Hello from Opcode Sample API!")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
