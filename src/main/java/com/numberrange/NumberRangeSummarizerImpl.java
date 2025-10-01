package com.numberrange;

import java.util.*;

/**
 * Production-ready implementation of NumberRangeSummarizer with consistent algorithms.
 * 
 * Design principles:
 * - Consistent ArrayList+sort approach for predictable performance
 * - Simple, maintainable code over premature optimization
 * - Proper input validation and error handling
 * - Optional debug logging for production troubleshooting
 * 
 * Performance characteristics:
 * - Time Complexity: O(n log n) for sorting, O(n) for range building
 * - Space Complexity: O(n) with efficient memory usage
 * - Handles up to 100,000 character inputs safely
 * 
 * @author Keuran Kisten
 * @version 3.0.0
 */
public class NumberRangeSummarizerImpl implements NumberRangeSummarizer {
    
    // Production constraints - configurable in real environment
    private static final int MAX_INPUT_LENGTH = 100_000;
    private static final boolean DEBUG_ENABLED = Boolean.getBoolean("numberrange.debug");

    /**
     * Parses a comma-separated string of integers into a sorted, unique collection.
     * 
     * @param input comma-separated string of integers (e.g., "1,3,6,7,8")
     * @return sorted collection of unique integers; empty collection if input is null/empty
     * @throws IllegalArgumentException if input exceeds maximum allowed size
     */
    @Override
    public Collection<Integer> collect(String input) {
        validateInputSize(input);
        
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Split by commas and parse each number
        String[] parts = input.split(",");
        
        // Use ArrayList first for faster insertions, then sort once
        List<Integer> numberList = new ArrayList<>(parts.length);
        
        for (String part : parts) {
            String cleaned = part.trim();
            if (!cleaned.isEmpty()) {
                Integer number = tryParseInt(cleaned);
                if (number != null) {
                    numberList.add(number);
                }
            }
        }
        
        // Sort once and remove duplicates efficiently
        List<Integer> result = deduplicateAndSort(numberList);
        
        if (DEBUG_ENABLED) {
            System.out.printf("[DEBUG] Processed %d parts, found %d valid numbers, result size: %d%n", 
                            parts.length, numberList.size(), result.size());
        }
        
        return result;
    }

    /**
     * Converts a collection of integers into a compact range representation.
     * 
     * @param input collection of integers to summarize
     * @return formatted string with ranges (e.g., "1, 3, 6-8, 12-15"); empty string if input is null/empty
     */
    @Override
    public String summarizeCollection(Collection<Integer> input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Trust the interface: prepare numbers consistently
        List<Integer> numbers = prepareNumbers(input);
        if (numbers.isEmpty()) {
            return "";
        }

        // Walk through the numbers and build ranges
        List<String> ranges = new ArrayList<>();
        int rangeStart = numbers.get(0);
        int rangeEnd = rangeStart;
        
        for (int i = 1; i < numbers.size(); i++) {
            int current = numbers.get(i);
            
            if (isConsecutive(rangeEnd, current)) {
                rangeEnd = current; // Extend the current range
            } else {
                ranges.add(formatRange(rangeStart, rangeEnd)); // Save current range
                rangeStart = current; // Start a new range
                rangeEnd = current;
            }
        }
        
        // remember the last range
        ranges.add(formatRange(rangeStart, rangeEnd));
        
        return String.join(", ", ranges);
    }
    
    /**
     * Gets a clean, sorted list of unique numbers from any collection.
     * Uses consistent ArrayList+sort approach for optimal performance.
     */
    private List<Integer> prepareNumbers(Collection<Integer> input) {
        List<Integer> numbers = new ArrayList<>();
        
        for (Integer number : input) {
            if (number != null) {
                numbers.add(number);
            }
        }
        
        return deduplicateAndSort(numbers);
    }
    
    /**
     * Checks if two numbers are consecutive (differ by 1).
     */
    private boolean isConsecutive(int first, int second) {
        return second == first + 1;
    }
    
    /**
     * Formats a range. Single numbers stay as-is, ranges become "start-end".
     */
    private String formatRange(int start, int end) {
        if (start == end) {
            return String.valueOf(start);
        } else {
            return start + "-" + end;
        }
    }
    
    /**
     * Safely parses a string to Integer with error handling.
     * 
     * @param text string to parse
     * @return parsed Integer or null if invalid
     */
    private Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Validates input size to prevent performance issues and DoS attacks.
     * 
     * @param input input string to validate
     * @throws IllegalArgumentException if input exceeds limits
     */
    private void validateInputSize(String input) {
        if (input != null && input.length() > MAX_INPUT_LENGTH) {
            String message = String.format(
                "Input size exceeds limit: %d characters (maximum allowed: %d). " +
                "Consider breaking large inputs into smaller chunks.",
                input.length(), MAX_INPUT_LENGTH);
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Efficiently sorts and removes duplicates from a list in a single pass.
     * More efficient than TreeSet for larger datasets.
     * 
     * @param numbers list to deduplicate and sort
     * @return new sorted list with unique elements
     */
    private List<Integer> deduplicateAndSort(List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return new ArrayList<>();
        }
        
        Collections.sort(numbers);
        
        // Remove duplicates in-place for memory efficiency
        List<Integer> result = new ArrayList<>(numbers.size());
        Integer previous = null;
        
        for (Integer current : numbers) {
            if (!current.equals(previous)) {
                result.add(current);
                previous = current;
            }
        }
        
        return result;
    }
    
}
