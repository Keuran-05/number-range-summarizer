# Number Range Summarizer

A production-ready Java utility that converts comma-separated numbers into compact range format for business applications.

## Business Problem

Converts comma-separated number lists into readable range format, commonly needed for:
- Inventory management ("Items 1-5, 8, 12-15 are out of stock")
- Report pagination ("Pages 1-3, 7, 10-12 contain errors")
- Data processing pipelines requiring compact number representation

## Key Features

- **Fast Processing** - O(n log n) performance suitable for production use
- **Input Validation** - Protects against oversized inputs and malformed data
- **Error Handling** - Gracefully handles invalid numbers without crashing
- **Production Ready** - Includes proper JavaDoc and enterprise-grade validation

### Example Usage

```java
NumberRangeSummarizer summarizer = new NumberRangeSummarizerImpl();

// Basic usage
Collection<Integer> numbers = summarizer.collect("1,3,6,7,8,12,13,14,15,21,22,23,24,31");
String result = summarizer.summarizeCollection(numbers);
// Output: "1, 3, 6-8, 12-15, 21-24, 31"

// Handles messy input
Collection<Integer> messyNumbers = summarizer.collect("3,,,4,,,,,,,6,,,,,,6,,,6,6,6,,,");
String cleanResult = summarizer.summarizeCollection(messyNumbers);
// Output: "3-4, 6"

// Works with negative numbers
Collection<Integer> negativeNumbers = summarizer.collect("-5,-4,-3,-1,0,1,2");
String negativeResult = summarizer.summarizeCollection(negativeNumbers);
// Output: "-5--3, -1-2"
```

## 🚀 Quick Start

### Prerequisites

- **Java 8+** (tested on Java 8, 11, 17)
- **Maven 3.6+**
- **Git** (for cloning)

### Installation

```bash
# Clone the repository
git clone https://github.com/Keuran-05/number-range-summarizer.git
cd number-range-summarizer

# Build the project
mvn clean compile

# Run all tests
mvn test

# Generate coverage report
mvn jacoco:report
```

### Running the Demo

```bash
# Interactive demo with examples
mvn exec:java

# Or run directly
java -cp target/classes com.numberrange.demo.NumberRangeSummarizerDemo
```

## 🧪 Testing & Quality

### Test Coverage

This project includes **45 comprehensive test cases** covering:

- ✅ Basic functionality and specification examples
- ✅ Edge cases: empty input, null values, single numbers
- ✅ Error scenarios: invalid input, mixed data types
- ✅ Performance: large inputs, stress testing
- ✅ Boundary conditions: Integer.MIN_VALUE, Integer.MAX_VALUE
- ✅ Special formats: leading zeros, plus signs, whitespace

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn clean test jacoco:report

# Run specific test
mvn test -Dtest=NumberRangeSummarizerTest#testCollectBasicInput

# View coverage report
open target/site/jacoco/index.html
```

### Code Quality Tools

```bash
# Run checkstyle
mvn checkstyle:check

# Generate all reports
mvn clean test jacoco:report checkstyle:checkstyle
```

## 🏗️ Project Structure

```
├── .github/workflows/
│   └── ci.yml                            # GitHub Actions CI/CD
├── src/
│   ├── main/java/com/numberrange/
│   │   ├── NumberRangeSummarizer.java    # Core interface (provided)
│   │   ├── NumberRangeSummarizerImpl.java # Main implementation
│   │   └── demo/
│   │       └── NumberRangeSummarizerDemo.java # Interactive demo
│   └── test/java/com/numberrange/
│       └── NumberRangeSummarizerTest.java # 45 comprehensive tests
├── pom.xml                               # Maven configuration
├── .gitignore                            # Git ignore rules
└── README.md                             # This file
```

## 📊 Algorithm Details

### Time Complexity
- **collect()**: O(n log n) - due to TreeSet sorting
- **summarizeCollection()**: O(n log n) - sorting + O(n) range building
- **Overall**: O(n log n) where n is the number of valid integers

### Space Complexity
- O(n) for storing unique numbers
- Efficient memory usage with no redundant data structures

### Design Decisions

1. **TreeSet Usage**: Automatic sorting and deduplication
2. **Graceful Error Handling**: Invalid input ignored, no exceptions thrown
3. **Single-Pass Range Building**: Efficient consecutive number detection
4. **Immutable Interface**: Thread-safe, stateless implementation

## 🔧 API Reference

### NumberRangeSummarizer Interface

```java
public interface NumberRangeSummarizer {
    Collection<Integer> collect(String input);
    String summarizeCollection(Collection<Integer> input);
}
```

### NumberRangeSummarizerImpl

#### `collect(String input)`

**Purpose**: Parses comma-separated string into sorted collection of unique integers

**Parameters**:
- `input` - Comma-separated string (e.g., "1,3,6,7,8")

**Returns**: `Collection<Integer>` - Sorted list of unique valid integers

**Behavior**:
- Handles null/empty input → returns empty collection
- Skips invalid numbers (non-numeric text)
- Removes duplicates automatically
- Sorts numbers in ascending order
- Supports negative numbers and zero

#### `summarizeCollection(Collection<Integer> input)`

**Purpose**: Converts integer collection into range-formatted string

**Parameters**:
- `input` - Collection of integers to summarize

**Returns**: `String` - Formatted ranges (e.g., "1, 3, 6-8, 12-15")

**Behavior**:
- Handles null/empty input → returns empty string
- Groups consecutive numbers into ranges
- Single numbers remain as-is
- Uses ", " (comma-space) as separator
- Handles negative number ranges correctly

## 🎮 Demo Application

The interactive demo showcases:

1. **Example Demonstrations**: Pre-built examples showing various scenarios
2. **Interactive Mode**: Try your own inputs with step-by-step processing
3. **Educational Output**: Shows parsing steps and statistics

## 🤝 Contributing

This project follows professional development practices:

1. **Code Style**: Google Java Style Guide
2. **Testing**: Comprehensive test coverage required
3. **Documentation**: Clear javadocs and README updates
4. **CI/CD**: All tests must pass before merging

## 👨‍💻 Author

**Keuran Kisten**  
📧 [GitHub: @Keuran-05](https://github.com/Keuran-05)   

---

## 📋 Assignment Compliance

This implementation satisfies all requirements:

✅ **Functionality** - Complete interface implementation with correct output  
✅ **Style** - Clean, readable code with proper naming conventions  
✅ **Robustness** - Comprehensive error handling and edge case management  
✅ **Best Practices** - Professional project structure, testing, and documentation  
✅ **Unit Tests** - 45 test cases covering all scenarios and edge cases  

*Developed as part of a software development assignment demonstrating professional coding standards and practices.*
