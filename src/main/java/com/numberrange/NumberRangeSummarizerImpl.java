package com.numberrange;

import java.util.*;

/**
 * High-performance implementation of NumberRangeSummarizer optimized for production use.
 * 
 * Features performance optimizations including:
 * - Single-pass sorting with efficient deduplication
 * - Smart detection of pre-sorted input to avoid unnecessary re-processing  
 * - Memory-efficient algorithms suitable for large datasets
 * - Input validation to prevent resource exhaustion
 * 
 * Time Complexity: O(n log n) for sorting, O(n) for range building
 * Space Complexity: O(n) with minimal overhead
 * 
 * @author Keuran Kisten
 */
public class NumberRangeSummarizerImpl implements NumberRangeSummarizer {
    
    // Production constraint for input validation
    private static final int MAX_INPUT_LENGTH = 100_000;

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
        return deduplicateAndSort(numberList);
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

        // Optimize: if input is already from our collect() method, it's sorted and unique
        List<Integer> numbers = (input instanceof ArrayList && isSortedAndUnique(input)) 
            ? new ArrayList<>(input) 
            : prepareNumbers(input);
            
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
     */
    private List<Integer> prepareNumbers(Collection<Integer> input) {
        Set<Integer> uniqueNumbers = new TreeSet<>();
        
        for (Integer number : input) {
            if (number != null) {
                uniqueNumbers.add(number);
            }
        }
        
        return new ArrayList<>(uniqueNumbers);
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
            throw new IllegalArgumentException(
                String.format("Input too large: %d characters (max: %d)", 
                            input.length(), MAX_INPUT_LENGTH));
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
    
    /**
     * Efficiently checks if collection is already sorted and contains unique elements.
     * Avoids unnecessary re-processing of data from our collect() method.
     * 
     * @param input collection to check
     * @return true if sorted and unique, false otherwise
     */
    private boolean isSortedAndUnique(Collection<Integer> input) {
        if (input.size() <= 1) return true;
        
        Integer previous = null;
        for (Integer current : input) {
            if (current == null) return false;
            if (previous != null && current <= previous) return false;
            previous = current;
        }
        return true;
    }
}
