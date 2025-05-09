package com.opcode.sample.controller;

import com.opcode.sample.model.DummyResponse;
import com.opcode.sample.service.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Dummy API", description = "Sample API endpoints for demonstration")
public class DummyController {

    private final DummyService dummyService;

    @Autowired
    public DummyController(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @GetMapping("/dummy")
    @Operation(
        summary = "Get dummy response",
        description = "Returns a dummy response with a message and timestamp",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DummyResponse.class)
                )
            )
        }
    )
    public ResponseEntity<DummyResponse> getDummyResponse() {
        return ResponseEntity.ok(dummyService.getDummyResponse());
    }
}
