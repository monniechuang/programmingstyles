package plugins.wordcounters;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import interfaces.WordCounter;

public class NormalWordCounter implements WordCounter {
    @Override
    public Map<String, Integer> countWords(List<String> words) {
        // Count the words
        Map<String, Integer> wordCounts = new HashMap<>();
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }
        
        // Return a map of the top 25 most frequent words, preserving the order
        return wordCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(25)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        Map.Entry::getValue, 
                        (e1, e2) -> e1, 
                        LinkedHashMap::new)); // Use LinkedHashMap to preserve order
    }
}
