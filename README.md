# Event Log Processor

## Overview

Event Log Processor is a Java application that processes event log files and generates aggregated statistics.

The application reads JSON events line by line, validates the data, processes valid events, handles invalid input gracefully, and produces a console-based statistics report.

The main goals of the project are:

* clean separation of responsibilities
* reliable error handling
* maintainable code structure
* testable components

## Features

The application provides:

* JSON event parsing
* Event validation
* Invalid input handling without stopping execution
* Event statistics aggregation
* User activity analysis
* Purchase statistics calculation
* Console report generation

Generated statistics include:

* Total valid events
* Total invalid lines
* Event count per user
* Purchase total, average, and largest purchase
* Most active user
* Top 3 most active users
* Event count per action

## Technologies Used

* Java 21
* Spring Boot
* Maven
* Jackson (JSON processing)
* JUnit 5
* AssertJ
* Lombok

## How to Run

### Requirements

Before running the application, make sure you have:

* Java 17 or higher installed
* Maven installed

### Running the application

Clone the repository:

```bash
git clone <repository-url>
```

Navigate to the project directory:

```bash
cd Event-Log-Processor
```

Run the application:

```bash
mvn spring-boot:run
```

The application will process the configured input file and print the generated statistics report to the console.

## Project Structure

```
src/main/java/com/ft/eventlogprocessor

├── model
│   ├── Event
│   └── Action
│
├── parser
│   └── EventParser
│
├── validation
│   └── EventValidator
│
├── statistics
│   ├── StatisticsAggregator
│   └── UserActivity
│
├── service
│   └── EventLogProcessor
│
└── report
    └── StatisticsReport
```

## Design Decisions

### Separation of Responsibilities

The application is divided into separate components with clear responsibilities:

**EventParser**

* Responsible for converting JSON strings into `Event` objects.

**EventValidator**

* Responsible for validating required fields and action-specific requirements.

**StatisticsAggregator**

* Responsible for processing valid events and maintaining statistics.

**StatisticsReport**

* Responsible for formatting and displaying the generated statistics.

**EventLogProcessor**

* Coordinates the processing workflow.

This approach keeps each component focused on a single responsibility and improves maintainability and testability.

### Enum-Based Actions

Event actions are represented using an enum:

```
LOGIN
LOGOUT
VIEW
CLICK
PURCHASE
```

Using an enum provides type safety and prevents unsupported actions from being processed after successful parsing.

### BigDecimal for Monetary Values

Purchase amounts are stored using `BigDecimal` instead of floating-point types.

This avoids precision problems that can occur when using `double` for financial calculations.

### Stream API Usage

Java Streams are used for operations such as:

* finding the most active user
* sorting users by activity
* transforming aggregated data for reporting

This keeps collection processing concise and readable.

## Validation Rules

An event is considered invalid when:

* the JSON format is invalid
* required fields are missing
* UUID values are invalid
* timestamp is missing or invalid
* an action-specific required field is missing

Action-specific requirements:

| Action   | Required Field |
| -------- | -------------- |
| VIEW     | articleId      |
| CLICK    | target         |
| PURCHASE | amount         |

Invalid events do not affect statistics calculations but are counted in the total invalid line count.

## Assumptions

The following assumptions were made during implementation:

* Input is provided as one JSON object per line.
* Processing continues after encountering invalid lines.
* Statistics are calculated using valid events only.
* Duplicate event IDs are not handled because deduplication was listed as an optional feature.
* The application processes the input sequentially.
* Top users are ordered by event count descending.

## Trade-offs Considered

### In-Memory Aggregation

The application stores statistics in memory while processing events.

Advantages:

* Simple implementation
* Fast access to aggregated values
* Suitable for the scope of this task

Trade-off:

* Very large datasets would require additional optimization such as external storage or streaming aggregation.

### Console Report Output

The application prints results directly to the console.

Advantages:

* Simple implementation
* Matches the requirements of the task

Trade-off:

* A REST API or external reporting system would be more suitable for a production environment.

### Sequential Processing

Events are processed sequentially.

Advantages:

* Simple and predictable execution flow
* Easier error handling

Trade-off:

* Large files could benefit from parallel processing or streaming improvements.

## Testing

Unit tests were implemented using JUnit 5 and AssertJ.

Test coverage includes:

* Event validation
* Required field validation
* Action-specific validation
* Statistics aggregation
* Purchase calculations
* Most active user calculation
* Top user ranking

All tests pass successfully.
