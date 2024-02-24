package week4;

/* 
Style #10
Constraints:
ExisTFTheOnece of an abstraction to which values can be converted.
This abstraction provides operations to (1) wrap around values, so that they become the abstraction; (2) bind itself to functions, so to establish sequences of functions; and (3) unwrap the value, so to examine the final result.
Larger problem is solved as a pipeline of functions bound together, with unwrapping happening at the end.
Particularly for The One style, the bind operation simply calls the given function, giving it the value that it holds, and holds on to the returned value.

Possible names:
The One
Monadic Identity
The wrapper of all things
Imperative functional style
*/

import java.io.File;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

public class Ten {
    interface IFunction {
        Object call(Object arg);
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                TFTheOne tfTheOne = new TFTheOne(args[0]);
                tfTheOne.bind(new LoadDataWords())
                        .bind(new RemoveStopWords())
                        .bind(new CountFrequencies())
                        .bind(new SortWords())
                        .bind(new PrintTF())
                        .printMe();
            } else {
                System.out.println("Error: file name must be passed as argument");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static class TFTheOne {
        private Object value;

        public TFTheOne(Object v) {
            value = v;
        }

        public TFTheOne bind(IFunction func) {
            value = func.call(value);
            return this;
        }

        public void printMe() {
            System.out.println(value);
        }
    }

    private static class LoadDataWords implements IFunction {
        public Object call(Object arg) {
            LinkedList<String> words = new LinkedList<>();
            String path = (String) arg;
            try {
                File file = new File(path);
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().toLowerCase();
                    line = line.replaceAll("[^a-z0-9]", " ");
                    String[] result = line.split("\\s+");
                    for (String word : result) {
                        if (word.length() > 1) {
                            words.add(word);
                        }
                    }
                }
                sc.close();
            } catch (IOException e) {
                System.out.println(e + " Couldn't open file " + path);
                System.exit(0);
            }
            return words;
        }
    }    

    private static class RemoveStopWords implements IFunction {
        public Object call(Object arg) {
            LinkedList<String> words = new LinkedList<>((LinkedList<String>) arg);
            Set<String> stopWords = new HashSet<>();
            try {
                File file = new File("stop_words.txt");
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] splitted = line.split(",");
                    Collections.addAll(stopWords, splitted);
                }
                sc.close();
            } catch (IOException e) {
                System.out.println(e + " couldn't open stop words file");
                System.exit(0);
            }
            words.removeIf(stopWords::contains);
            return words;
        }
    }

    private static class CountFrequencies implements IFunction {
        public Object call(Object arg) {
            LinkedHashMap<String, Integer> freqs = new LinkedHashMap<>();
            for (String word : (LinkedList<String>) arg) {
                freqs.put(word, freqs.getOrDefault(word, 0) + 1);
            }
            return freqs;
        }
    }

    private static class SortWords implements IFunction {
        public Object call(Object arg) {
            LinkedHashMap<String, Integer> freqs = (LinkedHashMap<String, Integer>) arg;
            return freqs.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
        }
    }

    private static class PrintTF implements IFunction {
        public Object call(Object arg) {
            LinkedHashMap<String, Integer> sortedFreqs = (LinkedHashMap<String, Integer>) arg;
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (Map.Entry<String, Integer> entry : sortedFreqs.entrySet()) {
                if (count < 25) {
                    sb.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
                    count++;
                } else {
                    break;
                }
            }
            return sb.toString();
        }
    }
}
