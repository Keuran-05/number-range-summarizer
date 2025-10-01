package com.numberrange.demo;

import com.numberrange.NumberRangeSummarizer;
import com.numberrange.NumberRangeSummarizerImpl;

import java.util.Collection;
import java.util.Scanner;

/**
 * Interactive demo application for the NumberRangeSummarizer.
 * Shows various examples and allows user input testing.
 * 
 * @author Keuran Kisten
 */
public class NumberRangeSummarizerDemo {
    
    private static final NumberRangeSummarizer summarizer = new NumberRangeSummarizerImpl();

    public static void main(String[] args) {
        System.out.println(repeatChar('=', 60));
        System.out.println("         NUMBER RANGE SUMMARIZER DEMO");
        System.out.println(repeatChar('=', 60));
        System.out.println();
        
        // Show some examples first
        runExamples();
        
        // Then allow interactive testing
        runInteractiveMode();
    }
    
    private static void runExamples() {
        System.out.println("📋 EXAMPLE DEMONSTRATIONS:");
        System.out.println(repeatChar('-', 30));
        
        // Example from specification
        demonstrateExample(
            "Specification Example", 
            "1,3,6,7,8,12,13,14,15,21,22,23,24,31",
            "Basic range summarization with mixed singles and ranges"
        );
        
        // Negative numbers
        demonstrateExample(
            "Negative Numbers", 
            "-5,-4,-3,-1,0,1,2,5",
            "Handling negative numbers and zero"
        );
        
        // Duplicates and messy input
        demonstrateExample(
            "Messy Input", 
            "3,,,4,,,,,,,6,,,,,,6,,,6,6,6,,,",
            "Multiple commas, duplicates, and whitespace"
        );
        
        // Mixed valid/invalid
        demonstrateExample(
            "Mixed Input", 
            "1,abc,3,xyz,5,invalid,7,8,hello,9",
            "Valid numbers mixed with invalid text"
        );
        
        // Large range
        demonstrateExample(
            "Large Range", 
            "1,2,3,4,5,6,7,8,9,10,15,16,17,18,19,20",
            "Multiple consecutive ranges"
        );
        
        System.out.println();
    }
    
    private static void demonstrateExample(String title, String input, String description) {
        System.out.println("🔸 " + title + ":");
        System.out.println("   Description: " + description);
        System.out.println("   Input:  \"" + input + "\"");
        
        Collection<Integer> numbers = summarizer.collect(input);
        String result = summarizer.summarizeCollection(numbers);
        
        System.out.println("   Parsed: " + numbers);
        System.out.println("   Result: \"" + result + "\"");
        System.out.println();
    }
    
    private static void runInteractiveMode() {
        System.out.println("🎮 INTERACTIVE MODE:");
        System.out.println(repeatChar('-', 20));
        System.out.println("Now you can try your own inputs!");
        System.out.println("Enter comma-separated numbers (or 'quit' to exit):");
        System.out.println();
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("➤ Enter numbers: ");
            String input = scanner.nextLine().trim();
            
            if ("quit".equalsIgnoreCase(input) || "exit".equalsIgnoreCase(input)) {
                break;
            }
            
            if (input.isEmpty()) {
                System.out.println("   (Empty input - try entering some numbers!)");
                continue;
            }
            
            try {
                // Show the process step by step
                System.out.println();
                System.out.println("   Processing: \"" + input + "\"");
                
                Collection<Integer> numbers = summarizer.collect(input);
                System.out.println("   Parsed numbers: " + numbers);
                
                String result = summarizer.summarizeCollection(numbers);
                System.out.println("   📄 Final result: \"" + result + "\"");
                
                // Show some stats
                if (!numbers.isEmpty()) {
                    System.out.println("   📊 Stats: " + numbers.size() + " unique numbers processed");
                }
                
            } catch (Exception e) {
                System.out.println("   ❌ Error: " + e.getMessage());
            }
            
            System.out.println();
        }
        
        scanner.close();
        System.out.println();
        System.out.println("Thanks for trying the Number Range Summarizer! 👋");
        System.out.println(repeatChar('=', 60));
    }
    
    /**
     * Helper method to repeat a character n times (for Java 8 compatibility).
     */
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
