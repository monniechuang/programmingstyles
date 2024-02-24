package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.Properties;

import interfaces.WordCounter;
import interfaces.WordExtractor;

public class Twenty {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Properties config = new Properties();
        config.load(new FileInputStream("config.properties"));

        WordExtractor extractor = (WordExtractor) Class.forName(config.getProperty("wordExtractor")).newInstance();
        WordCounter counter = (WordCounter) Class.forName(config.getProperty("wordCounter")).newInstance();

        List<String> words = extractor.extractWords(args[0]);
        Map<String, Integer> wordCounts = counter.countWords(words);

        wordCounts.forEach((word, count) -> System.out.println(word + " - " + count));


    }
}