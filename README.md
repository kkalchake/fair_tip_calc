# Tip Calculator

A production-ready tip calculator application written in Java 21 with comprehensive test coverage.

## Features

- Calculate tips based on percentage
- Display bill breakdown with tip amount and total
- Special "Thank you for your generosity!" message for tips > 25%
- Precise monetary calculations using BigDecimal
- Input validation and error handling
- 80%+ test coverage

## Requirements

- Java 21 or higher
- Maven 3.8+

## Build and Run
```bash
# Build the project
mvn clean install

# Run the application
java -jar target/tip-calculator-1.0.0.jar
```

## Run Tests
```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## Test Coverage

Current test coverage exceeds 80% with comprehensive unit tests.

## CI/CD

GitHub Actions workflow automatically:
- Builds the project
- Runs all tests
- Generates coverage reports
- Creates deployable artifacts