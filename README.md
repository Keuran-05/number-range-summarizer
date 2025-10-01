# Number Range Summarizer

A Java utility that transforms collections of numbers into compact, range representations.

## Overview

This project provides an implementation of a number range summarizer that takes comma-separated integers and groups consecutive numbers into ranges.

**Example:**
```
Input:  "1,3,6,7,8,12,13,14,15,21,22,23,24,31"
Output: "1, 3, 6-8, 12-15, 21-24, 31"
```

## Author

**Keuran Kisten** ([@Keuran-05](https://github.com/Keuran-05))

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6 or higher

### Building the Project

```bash
mvn clean compile
```

### Running Tests

```bash
mvn test
```

## Project Structure

```
src/
├── main/java/com/numberrange/
│   ├── NumberRangeSummarizer.java        # Core interface
│   ├── NumberRangeSummarizerImpl.java    # Implementation
│   └── demo/
│       └── NumberRangeSummarizerDemo.java # Demo application
└── test/java/com/numberrange/
    └── NumberRangeSummarizerTest.java     # Unit tests
```

## License

This project is developed as part of an assignment.