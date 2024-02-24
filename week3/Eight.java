package week3;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*
 using any language you want, including Python, 
 implement a recursive solution to the term frequency problem where parsing words from the stream of characters is recursive. 
 Much like the JSON parse function, your parse function should take a character-level file reader, 
 a list of words (initially empty), and the list of stop words, 
 and should return a list of the non-stop words. 
 The function should iterate to get the characters of one single word, 
 and then recurse to get the rest of the words. 
 You can do the rest of the program (counting words, printing them) in non-recursive style.
 */ 
 public class Eight {
 
     // A Set to hold the stop words that will be excluded from the word count.
     private static Set<String> stopWords = new HashSet<>();
 
     public static void main(String[] args) throws IOException {
         if (args.length != 1) {
             System.out.println("Usage: java eight <file_path>");
             System.exit(1);
         }
 
         // Load the file and process the text.
         String filePath = args[0];
         loadStopWords();
         List<String> words = parseWords(new BufferedReader(new FileReader(filePath)), new StringBuilder(), new ArrayList<>());
         Map<String, Integer> wordFrequencies = countWordFrequencies(words);
         displayTopWords(wordFrequencies, 25);
     }
 
     // Method to load stop words from a file into a Set.
     private static void loadStopWords() throws IOException {
         List<String> lines = Files.readAllLines(Paths.get("stop_words.txt"));
         for (String line : lines) {
             stopWords.addAll(Arrays.asList(line.split(",")));
         }
     }
 
     // Recursive method to parse words from a BufferedReader.
     private static List<String> parseWords(BufferedReader reader, StringBuilder currentWord, List<String> words) throws IOException {
         int ch = reader.read(); // Read a single character
         if (ch == -1) { // Check for the end of the stream
             if (currentWord.length() > 1) { // Check if the last word's length is greater than 1
                 addWordToList(currentWord.toString(), words);
             }
             return words;
         }
 
         char character = (char) ch;
         if (Character.isLetterOrDigit(character)) {
             currentWord.append(character); // Append character to the current word
         } else {
             if (currentWord.length() > 1) { // Check if the current word's length is greater than 1
                 addWordToList(currentWord.toString(), words); // Add the word to the list
             }
             currentWord.setLength(0); // Reset the current word
         }
 
         return parseWords(reader, currentWord, words); // Recursive call for the next character
     }    
 
     // Method to add a word to the list if it's not a stop word.
     private static void addWordToList(String word, List<String> words) {
         word = word.toLowerCase();
         if (!stopWords.contains(word)) {
             words.add(word);
         }
     }
 
     // Method to count the frequencies of each word in the list.
     private static Map<String, Integer> countWordFrequencies(List<String> words) {
         Map<String, Integer> frequencies = new HashMap<>();
         for (String word : words) {
             frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
         }
         return frequencies;
     }
 
     // Method to display the top N words based on their frequencies.
     private static void displayTopWords(Map<String, Integer> wordFrequencies, int topCount) {
         wordFrequencies.entrySet().stream()
             .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
             .limit(topCount)
             .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
     }
 }
 