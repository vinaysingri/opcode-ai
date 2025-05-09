package com.opcode;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class for the Opcode Microprocessor Simulator.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.opcode.core",
    "com.opcode.instruction",
    "com.opcode.parser",
    "com.opcode.service",
    "com.opcode.controller",
    "com.opcode.exception"
})
@OpenAPIDefinition(
    info = @Info(
        title = "Opcode Microprocessor Simulator API",
        version = "1.0",
        description = "REST API for simulating a custom microprocessor with a specific instruction set",
        contact = @Contact(
            name = "Development Team",
            email = "dev@opcode.com",
            url = "https://opcode.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    )
)
public class OpcodeSampleApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OpcodeSampleApplication.class, args);
    }
}
