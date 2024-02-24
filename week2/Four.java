package week2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Four {
    public static void main(String[] args) throws IOException {
        ArrayList<String[]> wordFreqs = new ArrayList<>();
        ArrayList<String> stopWords = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("stop_words.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            for (String word : line.split(",")) {
                stopWords.add(word.toLowerCase());
            }
        }
        for (char c = 'a'; c <= 'z'; c++) {
            stopWords.add(String.valueOf(c));
        }
        br.close();

        BufferedReader fileReader = new BufferedReader(new FileReader(args[0]));
        while ((line = fileReader.readLine()) != null) {
            line = line.toLowerCase() + "\n";
            int startChar = -1;
            int i = 0;
            for (char c : line.toCharArray()) {
                if (startChar == -1) {
                    if (Character.isLetterOrDigit(c)) {
                        startChar = i;
                    }
                } else {
                    if (!Character.isLetterOrDigit(c)) {
                        if (startChar < i - 1) {
                            String word = line.substring(startChar, i);
                            if (!stopWords.contains(word)) {
                                boolean found = false;
                                int pairIndex = 0;
                                for (String[] pair : wordFreqs) {
                                    if (word.equals(pair[0])) {
                                        pair[1] = String.valueOf(Integer.parseInt(pair[1]) + 1);
                                        found = true;
                                        break;
                                    }
                                    pairIndex++;
                                }
                                if (!found) {
                                    wordFreqs.add(new String[]{word, "1"});
                                } else {
                                    while (pairIndex > 0 && Integer.parseInt(wordFreqs.get(pairIndex)[1]) > Integer.parseInt(wordFreqs.get(pairIndex - 1)[1])) {
                                        String[] temp = wordFreqs.get(pairIndex);
                                        wordFreqs.set(pairIndex, wordFreqs.get(pairIndex - 1));
                                        wordFreqs.set(pairIndex - 1, temp);
                                        pairIndex--;
                                    }
                                }
                            }
                        }
                        startChar = -1;
                    }
                }
                i++;
            }
        }
        fileReader.close();

        for (int i = 0; i < 25 && i < wordFreqs.size(); i++) {
            System.out.println(wordFreqs.get(i)[0] + " - " + wordFreqs.get(i)[1]);
        }
    }
}