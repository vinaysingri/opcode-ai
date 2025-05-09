# Opcode Project – Requirements

## Overview

Microprocessors are typically programmed using an instruction set to achieve the desired logic for computation.  
We want to build a prototype of the instruction set for our custom microprocessor. We'll implement a subset of instructions in a simulator.

Our prototype microprocessor has **4 registers**: `A`, `B`, `C`, and `D`, each capable of storing **32-bit signed integers**.

---

## Instruction Set

| Instruction | Explanation       | Comments                                                                 |
|-------------|-------------------|--------------------------------------------------------------------------|
| SET A 10    | A = 10            | Sets register A to 10 (can be negative).                                 |
| ADR C D     | C = C + D         | Adds content of D into C. Result stored in C.                            |
| ADD A 12    | A = A + 12        | Adds 12 to register A. Acts as subtraction if value is negative.        |
| MOV A B     | A = B             | Sets A to the value of B. B remains unchanged.                           |
| INR C       | C = C + 1         | Increments C by 1.                                                       |
| DCR A       | A = A - 1         | Decrements A by 1.                                                       |
| RST         | A = B = C = D = 0 | Resets all registers to zero.                                           |

**References**:  
- [Signed vs Unsigned Integers – IBM Docs](https://www.ibm.com/docs/en/aix/7.2?topic=types-signed-unsigned-integers)

---

## Expectations

- Implement in one of these languages:
  - Java, Golang, Ruby, JavaScript, Python, or C#
  - create REST APIs.

- **Unit tests are required**.  
  Avoid using main/driver methods for testing.

- Focus on:
  - **Core logic and implementation**
  - Not presentation or API design

- Follow **best practices**:
  - SOLID principles
  - DRY (Don't Repeat Yourself)
  - Modular and extensible code

### Extensibility Considerations

- Future support for logical ops like AND, OR, XOR
- Adding new registers like `E`, `F`, etc.

---

## Evaluation Criteria

- Functional correctness
- Code quality, readability, and extensibility
