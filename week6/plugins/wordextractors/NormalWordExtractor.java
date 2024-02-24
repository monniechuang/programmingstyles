package plugins.wordextractors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import interfaces.WordExtractor;

public class NormalWordExtractor implements WordExtractor {
    @Override
    public List<String> extractWords(String pathToFile) {
        try {
            Path filePath = Paths.get(pathToFile);
            Path stopWordsPath = Paths.get("../stop_words.txt");
            // Ensure to read stop words first
            Set<String> stopWords = new HashSet<>(Arrays.asList(new String(Files.readAllBytes(stopWordsPath)).split(",")));
            // Add single-letter words to stop words
            stopWords.addAll(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));

            // Read all lines from the file
            List<String> lines = Files.readAllLines(filePath);

            // Process each line, filter by stop words, and collect results
            List<String> words = lines.stream()
                    .flatMap(line -> Stream.of(line.toLowerCase().split("[^a-zA-Z]+"))) // Split using non-alphabetic characters
                    .filter(word -> word.length() > 1 && !stopWords.contains(word)) // Exclude stop words and single-character words
                    .collect(Collectors.toList());

            return words;
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }
}
