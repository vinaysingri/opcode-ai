package com.opcode.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Opcode Sample API",
        version = "1.0",
        description = "Sample Spring Boot API for Opcode"
    )
)
public class OpcodeSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpcodeSampleApplication.class, args);
    }
}
