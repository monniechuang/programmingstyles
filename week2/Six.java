package week2;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Six {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            Path filePath = Paths.get(args[0]);
            print_all(sort(frequencies(remove_stop_words(filter_chars_and_normalize(read_file(filePath))))).subList(0, 25));
        } else {
            System.out.println("No file path provided");
        }
    }

    static String read_file(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

    static List<String> filter_chars_and_normalize(String strData) {
        return Arrays.asList(strData.toLowerCase().split("[^a-zA-Z]+"));
    }

    static List<String> remove_stop_words(List<String> wordList) throws IOException {
        Set<String> stopWords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("stop_words.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.addAll(Arrays.asList(line.split(",")));
            }
        }
        stopWords.addAll(IntStream.rangeClosed('a', 'z').mapToObj(c -> String.valueOf((char) c)).collect(Collectors.toSet()));
        return wordList.stream().filter(word -> !stopWords.contains(word)).collect(Collectors.toList());
    }

    static Map<String, Long> frequencies(List<String> wordList) {
        return wordList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    static List<Map.Entry<String, Long>> sort(Map<String, Long> wordFreq) {
        return wordFreq.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(Collectors.toList());
    }

    static void print_all(List<Map.Entry<String, Long>> wordFreqs) {
        wordFreqs.forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}
