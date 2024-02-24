package week1;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class WordFrequencyCounter {
    public static void main(String[] args) {
        // Check if the correct number of arguments is provided
        if (args.length != 1) {
            System.err.println("Usage: java WordFrequencyCounter <filename>");
            System.exit(1);
        }

        // Retrieve the input file path from the arguments
        String inputFile = args[0];
        Path inputPath = Paths.get(inputFile);

        // Verify if the input file exists
        if (!Files.exists(inputPath)) {
            System.err.println("File not found: " + inputFile);
            System.exit(1);
        }

        Set<String> stopWords;
        try {
            stopWords = loadStopWords("stop_words.txt");
        } catch (IOException e) {
            System.err.println("Error reading stop_words.txt: " + e.getMessage());
            System.exit(1);
            return;
        }

        // Map to store word frequencies
        Map<String, Integer> wordCounts = new HashMap<>();
        try (Stream<String> lines = Files.lines(inputPath)) {
            lines.forEach(line -> {
                // Split the line into words based on non-alphabetic characters
                String[] words = line.toLowerCase().split("[^a-zA-Z]+");
                for (String word : words) {
                    // Check if the word is valid, not a stop word, and longer than 1 character
                    if (!word.isEmpty() && word.length() > 1 && !stopWords.contains(word)) {
                        wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                    }
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            System.exit(1);
        }

        // Sort and display the top 25 most frequent words
        wordCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }

    // Method to load stop words from a file
    private static Set<String> loadStopWords(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                    .map(line -> line.split(","))
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toSet());
    }
}
