# Active Context

## Current Focus
- Implementing the Opcode Microprocessor Simulator
- Completed Task 2: Execute Single Instruction API
- Next up: Task 3: Execute Batch Instructions API

## Recent Changes
1. Added comprehensive API testing documentation:
   - Created docs/testing/api_test_sequence.md
   - Documented complete test workflow
   - Added curl commands for all endpoints
   - Included troubleshooting tips
   - Added expected results

2. Implemented core instruction classes:
   - SET: Sets a register to a specific value
   - ADR: Adds content of one register to another
   - ADD: Adds a constant value to a register
   - MOV: Copies value from one register to another
   - INR: Increments a register by 1
   - DCR: Decrements a register by 1
   - RST: Resets all registers to zero

2. Created supporting components:
   - InstructionFactory for creating instruction instances
   - InstructionParser for parsing instruction text
   - Processor facade for coordinating execution
   - ProcessorService for business logic
   - REST controller with error handling

3. Added comprehensive testing:
   - Unit tests for all components
   - Integration tests for end-to-end flows
   - Test coverage for success and error scenarios

## Active Decisions
1. Using Command pattern for instruction implementation
2. Using Factory pattern for instruction creation
3. Using Spring dependency injection for component wiring
4. Implementing proper error handling with specific exception types
5. Following REST best practices for API design

## Important Patterns and Preferences
1. Code Organization:
   - Core domain in com.opcode.core
   - Instructions in com.opcode.instruction
   - API components in controller/service layers
   - Clear separation of concerns

2. Error Handling:
   - Custom exceptions for different error types
   - Global exception handler for consistent responses
   - HTTP status codes aligned with REST conventions

3. Testing Strategy:
   - Unit tests for individual components
   - Integration tests for end-to-end flows
   - Mock MVC for controller testing
   - TestRestTemplate for integration testing

## Recent Learnings
1. API Testing Documentation:
   - Organized testing sequence for complete coverage
   - Structured documentation with clear sections
   - Added troubleshooting guidelines
   - Included expected results for verification

2. Spring Boot dependency injection setup:
   - Need @Component annotations on beans
   - Constructor injection preferred over field injection
   - Proper component scanning configuration required

2. Error handling improvements:
   - Added specific handler for JSON parsing errors
   - Consistent error response format
   - Proper HTTP status codes for different errors

3. Testing insights:
   - MockMvc vs TestRestTemplate usage
   - Proper test organization and naming
   - Effective use of test utilities and assertions
