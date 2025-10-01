package com.numberrange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for NumberRangeSummarizerImpl.
 * Covers typical usage, edge cases, and error scenarios.
 * 
 * @author Keuran Kisten
 */
class NumberRangeSummarizerTest {

    private NumberRangeSummarizer summarizer;

    @BeforeEach
    void setUp() {
        summarizer = new NumberRangeSummarizerImpl();
    }

    // ===== COLLECT METHOD TESTS =====

    @Test
    @DisplayName("collect() should parse basic comma-separated numbers")
    void testCollectBasicInput() {
        Collection<Integer> result = summarizer.collect("1,3,6,7,8");
        assertEquals(Arrays.asList(1, 3, 6, 7, 8), result);
    }

    @Test
    @DisplayName("collect() should handle single number")
    void testCollectSingleNumber() {
        Collection<Integer> result = summarizer.collect("5");
        assertEquals(Arrays.asList(5), result);
    }

    @Test
    @DisplayName("collect() should sort numbers automatically")
    void testCollectUnsortedInput() {
        Collection<Integer> result = summarizer.collect("8,1,6,3,7");
        assertEquals(Arrays.asList(1, 3, 6, 7, 8), result);
    }

    @Test
    @DisplayName("collect() should remove duplicates")
    void testCollectWithDuplicates() {
        Collection<Integer> result = summarizer.collect("1,3,3,7,7,8");
        assertEquals(Arrays.asList(1, 3, 7, 8), result);
    }

    @Test
    @DisplayName("collect() should handle negative numbers")
    void testCollectNegativeNumbers() {
        Collection<Integer> result = summarizer.collect("-3,-1,0,2");
        assertEquals(Arrays.asList(-3, -1, 0, 2), result);
    }

    @Test
    @DisplayName("collect() should handle whitespace around numbers")
    void testCollectWithWhitespace() {
        Collection<Integer> result = summarizer.collect(" 1 , 3 , 6 ");
        assertEquals(Arrays.asList(1, 3, 6), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t\n"})
    @DisplayName("collect() should return empty list for empty/whitespace input")
    void testCollectEmptyInput(String input) {
        Collection<Integer> result = summarizer.collect(input);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("collect() should return empty list for null input")
    void testCollectNullInput() {
        Collection<Integer> result = summarizer.collect(null);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("collect() should skip invalid numbers")
    void testCollectWithInvalidNumbers() {
        Collection<Integer> result = summarizer.collect("1,abc,3,xyz,5");
        assertEquals(Arrays.asList(1, 3, 5), result);
    }

    @Test
    @DisplayName("collect() should handle mixed valid/invalid input gracefully")
    void testCollectMixedInput() {
        Collection<Integer> result = summarizer.collect("1,,3, ,5,invalid,7");
        assertEquals(Arrays.asList(1, 3, 5, 7), result);
    }

    @Test
    @DisplayName("collect() should handle multiple consecutive commas with duplicates")
    void testCollectMultipleCommasWithDuplicates() {
        Collection<Integer> result = summarizer.collect("3,,,4,,,,,,,6,,,,,,6,,,6,6,6,,,");
        assertEquals(Arrays.asList(3, 4, 6), result);
    }

    @Test
    @DisplayName("collect() should handle numbers with leading zeros")
    void testCollectLeadingZeros() {
        Collection<Integer> result = summarizer.collect("01,02,003,10");
        assertEquals(Arrays.asList(1, 2, 3, 10), result);
    }

    @Test
    @DisplayName("collect() should handle numbers with plus signs")
    void testCollectPlusSigns() {
        Collection<Integer> result = summarizer.collect("+1,+2,3,-4");
        assertEquals(Arrays.asList(-4, 1, 2, 3), result);
    }

    @Test
    @DisplayName("collect() should handle only commas input")
    void testCollectOnlyCommas() {
        Collection<Integer> result = summarizer.collect(",,,,,");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("collect() should handle very large numbers")
    void testCollectVeryLargeNumbers() {
        Collection<Integer> result = summarizer.collect("2147483647,-2147483648,0");
        assertEquals(Arrays.asList(Integer.MIN_VALUE, 0, Integer.MAX_VALUE), result);
    }

    @Test
    @DisplayName("collect() should handle numbers that are too large for Integer")
    void testCollectNumbersTooLarge() {
        Collection<Integer> result = summarizer.collect("1,2147483648,3,-2147483649,4");
        assertEquals(Arrays.asList(1, 3, 4), result); // Large numbers should be ignored
    }

    // ===== SUMMARIZE COLLECTION METHOD TESTS =====

    @Test
    @DisplayName("summarizeCollection() should create ranges for consecutive numbers")
    void testSummarizeBasicRanges() {
        Collection<Integer> input = Arrays.asList(1, 3, 6, 7, 8, 12, 13, 14, 15);
        String result = summarizer.summarizeCollection(input);
        assertEquals("1, 3, 6-8, 12-15", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle single numbers")
    void testSummarizeSingleNumbers() {
        Collection<Integer> input = Arrays.asList(1, 3, 5, 7);
        String result = summarizer.summarizeCollection(input);
        assertEquals("1, 3, 5, 7", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle single number input")
    void testSummarizeSingleNumber() {
        Collection<Integer> input = Arrays.asList(5);
        String result = summarizer.summarizeCollection(input);
        assertEquals("5", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle two consecutive numbers")
    void testSummarizeTwoConsecutive() {
        Collection<Integer> input = Arrays.asList(5, 6);
        String result = summarizer.summarizeCollection(input);
        assertEquals("5-6", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle long consecutive sequences")
    void testSummarizeLongSequence() {
        Collection<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        String result = summarizer.summarizeCollection(input);
        assertEquals("1-10", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle negative number ranges")
    void testSummarizeNegativeRanges() {
        Collection<Integer> input = Arrays.asList(-5, -4, -3, -1, 0, 1, 2);
        String result = summarizer.summarizeCollection(input);
        assertEquals("-5--3, -1-2", result);
    }

    @Test
    @DisplayName("summarizeCollection() should return empty string for null input")
    void testSummarizeNullInput() {
        String result = summarizer.summarizeCollection(null);
        assertEquals("", result);
    }

    @Test
    @DisplayName("summarizeCollection() should return empty string for empty collection")
    void testSummarizeEmptyCollection() {
        String result = summarizer.summarizeCollection(new ArrayList<>());
        assertEquals("", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle unsorted input")
    void testSummarizeUnsortedInput() {
        Collection<Integer> input = Arrays.asList(8, 1, 6, 7, 3);
        String result = summarizer.summarizeCollection(input);
        assertEquals("1, 3, 6-8", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle duplicates in input")
    void testSummarizeWithDuplicates() {
        Collection<Integer> input = Arrays.asList(1, 1, 3, 3, 6, 7, 7, 8);
        String result = summarizer.summarizeCollection(input);
        assertEquals("1, 3, 6-8", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle null values in collection")
    void testSummarizeWithNullValues() {
        List<Integer> input = new ArrayList<>();
        input.add(1);
        input.add(null);
        input.add(3);
        input.add(null);
        input.add(6);
        
        String result = summarizer.summarizeCollection(input);
        assertEquals("1, 3, 6", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle collection with only null values")
    void testSummarizeOnlyNullValues() {
        List<Integer> input = Arrays.asList(null, null, null);
        String result = summarizer.summarizeCollection(input);
        assertEquals("", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle ranges that cross zero")
    void testSummarizeRangesCrossingZero() {
        Collection<Integer> input = Arrays.asList(-2, -1, 0, 1, 2, 5);
        String result = summarizer.summarizeCollection(input);
        assertEquals("-2-2, 5", result);
    }

    @Test
    @DisplayName("summarizeCollection() should handle alternating numbers (no ranges)")
    void testSummarizeAlternatingNumbers() {
        Collection<Integer> input = Arrays.asList(1, 3, 5, 7, 9, 11);
        String result = summarizer.summarizeCollection(input);
        assertEquals("1, 3, 5, 7, 9, 11", result);
    }

    // ===== INTEGRATION TESTS =====

    @Test
    @DisplayName("Full workflow: collect and summarize example from specification")
    void testFullWorkflowSpecificationExample() {
        String input = "1,3,6,7,8,12,13,14,15,21,22,23,24,31";
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("1, 3, 6-8, 12-15, 21-24, 31", result);
    }

    @Test
    @DisplayName("Full workflow: complex input with duplicates and invalid data")
    void testFullWorkflowComplexInput() {
        String input = "1,abc,3,3,6,invalid,7,8, ,12,13,xyz,14,15";
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("1, 3, 6-8, 12-15", result);
    }

    @Test
    @DisplayName("Full workflow: negative numbers with gaps")
    void testFullWorkflowNegativeNumbers() {
        String input = "-10,-9,-8,-5,-3,-2,-1,1,2,3,5";
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("-10--8, -5, -3--1, 1-3, 5", result);
    }

    @Test
    @DisplayName("Full workflow: single large range")
    void testFullWorkflowSingleRange() {
        String input = "1,2,3,4,5,6,7,8,9,10";
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("1-10", result);
    }

    @Test
    @DisplayName("Full workflow: multiple consecutive commas with duplicates")
    void testFullWorkflowMultipleCommas() {
        String input = "3,,,4,,,,,,,6,,,,,,6,,,6,6,6,,,";
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("3-4, 6", result);
    }

    @Test
    @DisplayName("Full workflow: mixed valid numbers with lots of junk")
    void testFullWorkflowMixedJunk() {
        String input = "abc,1,def,2.5,3,xyz,4,hello,5,world,6,7,test,8,9,10,garbage";
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("1, 3-10", result);
    }

    @Test
    @DisplayName("Full workflow: numbers with plus signs and leading zeros")
    void testFullWorkflowFormattedNumbers() {
        String input = "+01,+02,03,+4,05,+6,007,8,+09,010";
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("1-10", result);
    }

    @Test
    @DisplayName("Full workflow: only invalid input")
    void testFullWorkflowOnlyInvalid() {
        String input = "abc,def,xyz,hello,world,test,garbage,2.5,3.14";
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("", result);
    }

    // ===== EDGE CASE TESTS =====

    @Test
    @DisplayName("Edge case: Integer.MAX_VALUE and Integer.MIN_VALUE")
    void testEdgeCaseIntegerLimits() {
        Collection<Integer> input = Arrays.asList(Integer.MIN_VALUE, Integer.MIN_VALUE + 1, 
                                                Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
        String result = summarizer.summarizeCollection(input);
        assertTrue(result.contains(String.valueOf(Integer.MIN_VALUE)));
        assertTrue(result.contains(String.valueOf(Integer.MAX_VALUE)));
    }

    @Test
    @DisplayName("Edge case: large gaps between numbers")
    void testEdgeCaseLargeGaps() {
        Collection<Integer> input = Arrays.asList(1, 1000, 2000, 2001, 10000);
        String result = summarizer.summarizeCollection(input);
        assertEquals("1, 1000, 2000-2001, 10000", result);
    }

    @Test
    @DisplayName("Edge case: all numbers are the same")
    void testEdgeCaseAllSameNumbers() {
        Collection<Integer> input = Arrays.asList(5, 5, 5, 5, 5);
        String result = summarizer.summarizeCollection(input);
        assertEquals("5", result);
    }

    @Test
    @DisplayName("Performance: large input with many ranges")
    void testPerformanceLargeInput() {
        // Create input: 1-10, 20-30, 40-50, etc. (10 ranges of 10 numbers each)
        List<Integer> numbers = new ArrayList<>();
        for (int range = 0; range < 10; range++) {
            int start = range * 20 + 1;
            for (int i = start; i < start + 10; i++) {
                numbers.add(i);
            }
        }
        
        String result = summarizer.summarizeCollection(numbers);
        assertTrue(result.contains("1-10"));
        assertTrue(result.contains("21-30"));
        assertTrue(result.contains("181-190"));
    }

    @Test
    @DisplayName("Stress test: very long input string with lots of duplicates")
    void testStressVeryLongInput() {
        StringBuilder sb = new StringBuilder();
        // Create a string with thousands of duplicated numbers and commas
        for (int i = 0; i < 1000; i++) {
            sb.append("1,2,3,1,2,3,,,");
        }
        
        Collection<Integer> numbers = summarizer.collect(sb.toString());
        String result = summarizer.summarizeCollection(numbers);
        assertEquals("1-3", result);
    }
    
    @Test
    @DisplayName("Input validation: should reject oversized input")
    void testInputSizeValidation() {
        // Create input larger than MAX_INPUT_LENGTH (100,000)
        StringBuilder largeInput = new StringBuilder();
        for (int i = 0; i < 50001; i++) {
            largeInput.append("12,");
        }
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            summarizer.collect(largeInput.toString());
        });
        
        assertTrue(exception.getMessage().contains("Input too large"));
    }
    
    @Test
    @DisplayName("Performance: optimized pipeline should be efficient")
    void testOptimizedPerformance() {
        // Test the optimized collect -> summarize pipeline
        StringBuilder input = new StringBuilder();
        for (int i = 1; i <= 1000; i++) {
            if (i > 1) input.append(",");
            input.append(i);
        }
        
        long startTime = System.nanoTime();
        
        Collection<Integer> numbers = summarizer.collect(input.toString());
        String result = summarizer.summarizeCollection(numbers);
        
        long duration = System.nanoTime() - startTime;
        
        // Should handle 1000 numbers very quickly (under 50ms)
        assertTrue(duration < 50_000_000, "Processing should be fast: " + duration/1_000_000 + "ms");
        assertEquals("1-1000", result);
    }
}
