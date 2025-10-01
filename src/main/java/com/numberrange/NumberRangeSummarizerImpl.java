package com.numberrange;

import java.util.*;

/**
 * Takes comma-separated numbers and turns them into clean ranges.
 * Example: "1,3,6,7,8" becomes "1, 3, 6-8"
 * 
 * @author Keuran Kisten
 */
public class NumberRangeSummarizerImpl implements NumberRangeSummarizer {

    @Override
    public Collection<Integer> collect(String input) {
        // Nothing to work with? Return empty list
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Split by commas and parse each number
        String[] parts = input.split(",");
        Set<Integer> numbers = new TreeSet<>(); // TreeSet keeps things sorted and unique
        
        for (String part : parts) {
            String cleaned = part.trim();
            if (!cleaned.isEmpty()) {
                Integer number = tryParseInt(cleaned);
                if (number != null) {
                    numbers.add(number);
                }
            }
        }
        
        return new ArrayList<>(numbers);
    }

    @Override
    public String summarizeCollection(Collection<Integer> input) {
        // Handle empty cases upfront
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Make sure we have a clean, sorted list to work with
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
        
        // Don't forget the last range
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
     * Tries to parse a string as an integer. Returns null if it fails.
     * Just skips invalid input.
     */
    private Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null; // Just ignore invalid numbers
        }
    }
}
