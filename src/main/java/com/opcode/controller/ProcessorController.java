package com.opcode.controller;

import com.opcode.model.InstructionRequest;
import com.opcode.model.ProcessorResponse;
import com.opcode.service.ProcessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * REST controller for executing instructions on the microprocessor.
 */
@RestController
@RequestMapping("/api/v1")
public class ProcessorController {
    
    private final ProcessorService processorService;
    
    /**
     * Constructs a new ProcessorController with the specified service.
     *
     * @param processorService the service to use
     */
    public ProcessorController(ProcessorService processorService) {
        this.processorService = processorService;
    }
    
    /**
     * Executes a single instruction.
     *
     * @param request the instruction request
     * @return the execution result with updated register values
     */
    @Operation(summary = "Execute a single instruction",
               description = "Executes a single instruction and returns the updated register values")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instruction executed successfully",
                    content = @Content(schema = @Schema(implementation = ProcessorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid instruction syntax",
                    content = @Content(schema = @Schema(implementation = ProcessorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Invalid register referenced",
                    content = @Content(schema = @Schema(implementation = ProcessorResponse.class)))
    })
    @PostMapping("/instructions")
    public ResponseEntity<ProcessorResponse> executeInstruction(@Valid @RequestBody InstructionRequest request) {
        Map<String, Integer> registers = processorService.executeInstruction(request.getInstruction());
        return ResponseEntity.ok(ProcessorResponse.success(registers));
    }
}
