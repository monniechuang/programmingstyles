package week4;

/*
Style #15
Constraints:
Larger problem is decomposed into entities using some form of abstraction (objects, modules or similar)
The entities are never called on directly for actions
The entities provide interfaces for other entities to be able to register callbacks
At certain points of the computation, the entities call on the other entities that have registered for callbacks

Possible names:
Hollywood agent: "don't call us, we'll call you"
Inversion of control
Callback heaven/hell
*/

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

interface LoadEvent {
    void load(String path);
}

interface DoWorkEvent {
    void doWork();
}

interface EndEvent {
    void end();
}

class WordFrequencyFramework {
    private List<LoadEvent> loadEventHandlers = new ArrayList<>();
    private List<DoWorkEvent> doWorkEventHandlers = new ArrayList<>();
    private List<EndEvent> endEventHandlers = new ArrayList<>();

    public void registerForLoadEvent(LoadEvent handler) {
        loadEventHandlers.add(handler);
    }

    public void registerForDoWorkEvent(DoWorkEvent handler) {
        doWorkEventHandlers.add(handler);
    }

    public void registerForEndEvent(EndEvent handler) {
        endEventHandlers.add(handler);
    }

    public void run(String pathToFile) {
        loadEventHandlers.forEach(h -> h.load(pathToFile));
        doWorkEventHandlers.forEach(DoWorkEvent::doWork);
        endEventHandlers.forEach(EndEvent::end);
    }
}

class DataStorage {
    private String data = "";
    private StopWordFilter stopWordFilter;
    private List<Consumer<String>> wordEventHandlers = new ArrayList<>();

    public DataStorage(WordFrequencyFramework wfApp, StopWordFilter stopWordFilter) {
        this.stopWordFilter = stopWordFilter;
        wfApp.registerForLoadEvent(this::load);
        wfApp.registerForDoWorkEvent(this::produceWords);
    }

    private void load(String pathToFile) {
        try {
            data = new String(Files.readAllBytes(Paths.get(pathToFile))).toLowerCase();
            data = data.replaceAll("[\\W_]+", " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void produceWords() {
        Arrays.stream(data.split("\\s+"))
                .filter(word -> !stopWordFilter.isStopWord(word))
                .forEach(word -> wordEventHandlers.forEach(handler -> handler.accept(word)));
    }

    public void registerForWordEvent(Consumer<String> handler) {
        wordEventHandlers.add(handler);
    }
}

class StopWordFilter {
    private Set<String> stopWords = new HashSet<>();

    public StopWordFilter(WordFrequencyFramework wfApp) {
        wfApp.registerForLoadEvent(this::load);
    }

    private void load(String ignore) {
        try {
            stopWords = new HashSet<>(Arrays.asList(new String(Files.readAllBytes(Paths.get("stop_words.txt"))).split(",")));
            stopWords.addAll(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }
}

class WordFrequencyCounter {
    private Map<String, Integer> wordFreqs = new HashMap<>();

    public WordFrequencyCounter(WordFrequencyFramework wfApp, DataStorage dataStorage) {
        dataStorage.registerForWordEvent(this::incrementCount);
        wfApp.registerForEndEvent(this::printFreqs);
    }

    private void incrementCount(String word) {
        wordFreqs.put(word, wordFreqs.getOrDefault(word, 0) + 1);
    }

    private void printFreqs() {
        wordFreqs.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}

public class Fifteen {
    public static void main(String[] args) {
        WordFrequencyFramework wfApp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfApp);
        DataStorage dataStorage = new DataStorage(wfApp, stopWordFilter);
        WordFrequencyCounter wordFreqCounter = new WordFrequencyCounter(wfApp, dataStorage);
        wfApp.run(args[0]);
    }
}
