package week7;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// done with Java stream API or C# LINQ
public class Streams {

    private static Stream<String> lines(String filename) {
        try {
            return Files.lines(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    private static Stream<String> words(String filename) {
        return lines(filename)
                .flatMap(line -> Arrays.stream(line.split("[^a-zA-Z]")))
                .map(String::toLowerCase)
                .filter(word -> !word.isEmpty());
    }
    
    private static Set<String> loadStopWords() {
        try {
            String stopwordsContent = new String(Files.readAllBytes(Paths.get("stop_words.txt")));
            Set<String> stopwords = new HashSet<>(Arrays.asList(stopwordsContent.split(",")));
            stopwords.addAll(IntStream.rangeClosed('a', 'z')
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.toSet()));
            return stopwords;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }   

    private static Stream<String> nonStopWords(String filename) {
        Set<String> stopwords = loadStopWords();
        return words(filename).filter(word -> !stopwords.contains(word));
    }

    private static void countAndSort(String filename) {
        nonStopWords(filename)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            countAndSort(args[0]);
        } else {
            System.out.println("Please provide a filename as an argument.");
        }
    }
}
