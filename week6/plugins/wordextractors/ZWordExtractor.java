package plugins.wordextractors;

import java.util.List;
import java.util.stream.Collectors;


public class ZWordExtractor extends NormalWordExtractor {
    @Override
    public List<String> extractWords(String pathToFile) {
        return super.extractWords(pathToFile).stream()
                .filter(word -> word.contains("z"))
                .collect(Collectors.toList());
    }
}
