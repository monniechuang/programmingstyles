package plugins.wordcounters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.WordCounter;

public class LetterWordCounter implements WordCounter {
    @Override
    public Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> letterCounts = new HashMap<>();
        for (String word : words) {
            String firstLetter = word.substring(0, 1);
            letterCounts.put(firstLetter, letterCounts.getOrDefault(firstLetter, 0) + 1);
        }
        return letterCounts;
    }
}
