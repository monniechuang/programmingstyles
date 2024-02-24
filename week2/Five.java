package week2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Five {
    static List<String> text = new ArrayList<>();
    static List<String> filteredWords = new ArrayList<>();
    static Set<String> stopWords = new HashSet<>();
    static Map<String, Integer> wordFreqs = new HashMap<>();
    static List<Map.Entry<String, Integer>> sortedList = new LinkedList<>();

    public static void read_file(String fileName) {
        try {
            text = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load_stop_words() {
        try {
            BufferedReader stopWordReader = new BufferedReader(new FileReader("stop_words.txt"));
            String stopWordsLine = stopWordReader.readLine();
            if (stopWordsLine != null) {
                String[] stopWordArray = stopWordsLine.split(",");
                stopWords.addAll(Arrays.asList(stopWordArray));
            }
            stopWordReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void filter_and_normalize() {
        load_stop_words();
        for (String line : text) {
            for (String word : line.split("[^a-zA-Z]+")) {
                String lowercaseWord = word.toLowerCase();
                if (!stopWords.contains(lowercaseWord) && lowercaseWord.length() > 1) {
                    filteredWords.add(lowercaseWord);
                }
            }
        }
    }

    public static void frequencies() {
        for (String word : filteredWords) {
            wordFreqs.put(word, wordFreqs.getOrDefault(word, 0) + 1);
        }
    }

    public static void sort() {
        sortedList = new LinkedList<>(wordFreqs.entrySet());
        sortedList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    }

    public static void print() {
        for (Map.Entry<String, Integer> entry : sortedList.subList(0, Math.min(25, sortedList.size()))) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        read_file(args[0]);
        filter_and_normalize();
        frequencies();
        sort();
        print();
    }
}