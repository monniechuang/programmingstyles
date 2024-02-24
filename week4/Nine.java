package week4;

/* Style #9
Variation of the candy factory style, with the following additional constraints:

Each function takes an additional parameter, usually the last, which is another function
That function parameter is applied at the end of the current function
That function parameter is given as input what would be the output of the current function
Larger problem is solved as a pipeline of functions, but where the next function to be applied is given as parameter to the current function

Possible names:
Kick your teammate forward!
Continuation-passing style
Crochet loop */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

interface IFunction {
    void call(Object o, IFunction f);
}

public class Nine {

    public static void main(String[] args) {
        ReadFile readFile = new ReadFile();
        readFile.call(args[0], new FilterAndNormalize());
    }

    static class ReadFile implements IFunction {
        @Override
        public void call(Object fileName, IFunction filterAndNormalize) {
            try {
                List<String> text = Files.readAllLines(Paths.get((String) fileName));
                filterAndNormalize.call(text, new CountFrequencies());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class FilterAndNormalize implements IFunction {
        @Override
        public void call(Object text, IFunction countFrequencies) {
            Set<String> stops = new HashSet<>();
            try (BufferedReader stopW = new BufferedReader(new FileReader("stop_words.txt"))) {
                Collections.addAll(stops, stopW.readLine().split(","));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            List<String> words = new ArrayList<>();
            ((List<String>) text).forEach(line -> Arrays.stream(line.split("[^a-zA-Z]"))
                    .filter(word -> word.length() > 1 && !stops.contains(word.toLowerCase()))
                    .forEach(word -> words.add(word.toLowerCase())));
            countFrequencies.call(words, new SortWords());
        }
    }

    static class CountFrequencies implements IFunction {
        @Override
        public void call(Object words, IFunction sortWords) {
            Map<String, Integer> wordFreqs = new HashMap<>();
            ((List<String>) words).forEach(word ->
                    wordFreqs.put(word, wordFreqs.getOrDefault(word, 0) + 1));
            sortWords.call(wordFreqs, new PrintTf());
        }
    }

    static class SortWords implements IFunction {
        @Override
        public void call(Object wordFreqs, IFunction printTf) {
            List<Map.Entry<String, Integer>> sortedList = ((Map<String, Integer>) wordFreqs).entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toList());
            printTf.call(sortedList, new NoOp());
        }
    }

    static class PrintTf implements IFunction {
        @Override
        public void call(Object sortedList, IFunction f) {
            ((List<Map.Entry<String, Integer>>) sortedList).stream()
                    .limit(25)
                    .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
        }
    }

    static class NoOp implements IFunction {
        @Override
        public void call(Object o, IFunction f) {
            // Do nothing
        }
    }
}