package week7;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;


// Done with iterators that mimic the Python reference implementation (with generators)
public class Iterators {

    private static Iterator<Character> characters(String filename) {
        return new Iterator<Character>() {
            private BufferedReader reader;
            private int nextChar = -1;

            {
                try {
                    reader = new BufferedReader(new FileReader(filename));
                    nextChar = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public boolean hasNext() {
                return nextChar != -1;
            }

            public Character next() {
                int currentChar = nextChar;
                try {
                    nextChar = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return (char) currentChar;
            }
        };
    }

    private static Iterator<String> allWords(String filename) {
        return new Iterator<String>() {
            private Iterator<Character> charIterator = characters(filename);
            private String nextWord = advance();

            private String advance() {
                StringBuilder wordBuilder = new StringBuilder();
                while (charIterator.hasNext()) {
                    char c = charIterator.next();
                    if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                        wordBuilder.append(Character.toLowerCase(c));
                    } else if (wordBuilder.length() > 0) {
                        return wordBuilder.toString();
                    }
                }
                return wordBuilder.length() > 0 ? wordBuilder.toString() : null;
            }

            public boolean hasNext() {
                return nextWord != null;
            }

            public String next() {
                String currentWord = nextWord;
                nextWord = advance();
                return currentWord;
            }
        };
    }

    private static Iterator<String> nonStopWords(String filename) {
        Set<String> stopwords = new HashSet<>();
        try {
            stopwords.addAll(Arrays.asList(new BufferedReader(new FileReader("stop_words.txt"))
                    .readLine().split(",")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopwords.addAll(IntStream.rangeClosed('a', 'z')
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.toSet()));

        return new Iterator<String>() {
            private Iterator<String> wordIterator = allWords(filename);
            private String nextValidWord = advance();

            private String advance() {
                while (wordIterator.hasNext()) {
                    String word = wordIterator.next();
                    if (!stopwords.contains(word)) {
                        return word;
                    }
                }
                return null;
            }

            public boolean hasNext() {
                return nextValidWord != null;
            }

            public String next() {
                String currentWord = nextValidWord;
                nextValidWord = advance();
                return currentWord;
            }
        };
    }

    private static Iterator<Map.Entry<String, Integer>> countAndSort(String filename) {
        return new Iterator<Map.Entry<String, Integer>>() {
            private Iterator<String> wordIterator = nonStopWords(filename);
            private Map<String, Integer> freqs = new HashMap<>();
            private Iterator<Map.Entry<String, Integer>> sortedIterator = null;

            private void process() {
                while (wordIterator.hasNext()) {
                    String word = wordIterator.next();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                sortedIterator = freqs.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .iterator();
            }

            public boolean hasNext() {
                if (sortedIterator == null) {
                    process();
                }
                return sortedIterator.hasNext();
            }

            public Map.Entry<String, Integer> next() {
                if (sortedIterator == null) {
                    process();
                }
                return sortedIterator.next();
            }
        };
    }

    public static void main(String[] args) {
        Iterator<Map.Entry<String, Integer>> wordFreqs = countAndSort(args[0]);
        int count = 0;
        while (wordFreqs.hasNext() && count < 25) {
            Map.Entry<String, Integer> entry = wordFreqs.next();
            System.out.println(entry.getKey() + " - " + entry.getValue());
            count++;
        }
    }
}
