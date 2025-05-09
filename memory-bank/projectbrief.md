# Project Brief: Opcode Microprocessor Simulator

## Project Overview

The Opcode Microprocessor Simulator is a software implementation that simulates a custom microprocessor with a specific instruction set. This project aims to build a prototype of the instruction set for a custom microprocessor, implementing a subset of instructions in a simulator.

## Core Requirements

1. **Microprocessor Specifications**:
   - 4 registers: `A`, `B`, `C`, and `D`
   - Each register capable of storing 32-bit signed integers

2. **Instruction Set**:
   | Instruction | Explanation       | Comments                                                  |
   |-------------|-------------------|-----------------------------------------------------------|
   | SET A 10    | A = 10            | Sets register A to 10 (can be negative).                  |
   | ADR C D     | C = C + D         | Adds content of D into C. Result stored in C.             |
   | ADD A 12    | A = A + 12        | Adds 12 to register A. Acts as subtraction if negative.   |
   | MOV A B     | A = B             | Sets A to the value of B. B remains unchanged.            |
   | INR C       | C = C + 1         | Increments C by 1.                                        |
   | DCR A       | A = A - 1         | Decrements A by 1.                                        |
   | RST         | A = B = C = D = 0 | Resets all registers to zero.                             |

3. **Implementation Requirements**:
   - Implement in Java with Spring Boot
   - Create REST APIs to interact with the simulator
   - Include comprehensive unit tests
   - Follow best practices (SOLID principles, DRY)

4. **API Endpoints**:
   - Execute a single instruction
   - Execute multiple instructions in batch
   - Get register values (all or specific)
   - Reset processor state

## Project Goals

1. Create a functional simulator that accurately implements the specified instruction set
2. Design the system to be modular and extensible for future enhancements
3. Implement a clean, RESTful API for interacting with the simulator
4. Ensure code quality through comprehensive testing and adherence to best practices

## Extensibility Considerations

The system should be designed to easily accommodate:
- Future support for logical operations (AND, OR, XOR)
- Addition of new registers (E, F, etc.)

## Evaluation Criteria

The project will be evaluated based on:
- Functional correctness
- Code quality, readability, and extensibility
- Adherence to best practices
- Comprehensive test coverage
