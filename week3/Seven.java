package week3;

import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/*
Style #7
Constraints:
- As few lines of code as possible

Possible names:
- Code golf
- Try hard
*/

public class Seven {
    public static void main(String[] args) throws Exception {
        Set<String> stopWords = new HashSet<>(Arrays.asList(new String(Files.readAllBytes(Paths.get("stop_words.txt"))).split(",")));
        Files.lines(Paths.get(args[0]))
            .flatMap(line -> Arrays.stream(line.toLowerCase().split("[^a-zA-Z]+")))
            .filter(word -> word.length() > 1 && !stopWords.contains(word))
            .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
            .limit(25)
            .forEach(e -> System.out.println(e.getKey() + " - " + e.getValue()));
    }
}