package com.opcode.sample.controller;

import com.opcode.sample.model.DummyResponse;
import com.opcode.sample.service.DummyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DummyController.class)
class DummyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DummyService dummyService;

    @Test
    void getDummyResponse_ShouldReturnDummyResponse() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        DummyResponse dummyResponse = DummyResponse.builder()
                .message("Test message")
                .timestamp(now)
                .build();
        
        when(dummyService.getDummyResponse()).thenReturn(dummyResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/dummy")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test message"));
    }
}
