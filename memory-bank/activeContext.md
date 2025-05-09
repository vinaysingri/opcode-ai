# Active Context: Opcode Microprocessor Simulator

## Current Work Focus

The project is currently in the initial setup phase. The basic Spring Boot application structure has been created with a dummy controller, service, and model. The focus now is on implementing the core domain model and the first API endpoint for executing a single instruction.

### Current Task

**Task 1: Core Domain Model Implementation**

This task involves:
- Implementing the register manager and basic exception handling
- Creating the instruction interface and abstract base class
- Writing unit tests for the core components

## Recent Changes

1. **Project Initialization**
   - Created Spring Boot application structure
   - Set up Gradle build configuration
   - Added Swagger/OpenAPI documentation support
   - Implemented a dummy controller for testing the setup

2. **Documentation**
   - Created comprehensive technical specification
   - Defined API specification
   - Developed implementation plan with task breakdown
   - Established memory bank for project documentation

## Next Steps

1. **Complete Task 1: Core Domain Model Implementation**
   - Implement `RegisterManager` class
   - Create `Instruction` interface and `AbstractInstruction` class
   - Implement exception classes
   - Write unit tests for these components

2. **Proceed to Task 2: Execute Single Instruction API**
   - Implement concrete instruction classes (SET, ADR, ADD, MOV, INR, DCR, RST)
   - Create instruction factory and parser
   - Implement processor core functionality
   - Create service method for executing a single instruction
   - Implement POST /api/v1/instructions endpoint
   - Write unit and integration tests

## Active Decisions and Considerations

1. **Package Structure**
   - Decided to use a feature-based package structure
   - Core domain logic will be in `com.opcode.sample.core`
   - Instructions will be in `com.opcode.sample.instruction`
   - Exception handling will be in `com.opcode.sample.exception`

2. **Testing Strategy**
   - Using JUnit 5 for unit testing
   - MockMvc for controller testing
   - Spring Boot Test for integration testing
   - Aiming for high test coverage, especially for instruction execution logic

3. **API Design Considerations**
   - RESTful endpoints following best practices
   - Clear request/response formats
   - Comprehensive error handling
   - Swagger/OpenAPI documentation

4. **Implementation Approach**
   - Following the implementation plan tasks in sequence
   - Using TDD approach where appropriate
   - Focusing on clean, maintainable code
   - Ensuring extensibility for future enhancements

## Important Patterns and Preferences

1. **Code Style**
   - Using Java 17 features where appropriate
   - Following Spring Boot conventions
   - Leveraging Lombok to reduce boilerplate
   - Clear naming conventions for classes, methods, and variables

2. **Design Patterns in Use**
   - Command Pattern for instructions
   - Factory Pattern for instruction creation
   - Registry Pattern for register management
   - Facade Pattern for processor interface

3. **Error Handling**
   - Custom exceptions for specific error scenarios
   - Global exception handler for consistent API responses
   - Detailed error messages for debugging

4. **Documentation**
   - Comprehensive Javadoc comments
   - Swagger/OpenAPI annotations for API documentation
   - README with setup and usage instructions

## Learnings and Project Insights

1. **Key Insights**
   - The command pattern provides a clean way to implement the various instructions
   - Using a factory for instruction creation simplifies adding new instructions
   - The register manager abstraction makes it easy to extend with new registers

2. **Challenges**
   - Ensuring proper validation of instruction syntax
   - Handling edge cases like integer overflow/underflow
   - Maintaining clean separation of concerns

3. **Opportunities for Improvement**
   - Consider adding logging for instruction execution
   - Explore performance optimizations for batch instruction execution
   - Investigate adding support for instruction history/undo
