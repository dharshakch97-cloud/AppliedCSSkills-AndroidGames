/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.*;
import java.util.*;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<Integer, ArrayList<String>> sizeToWords;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);

            if (lettersToWord.containsKey(sortedWord))
                lettersToWord.get(sortedWord).add(word);
            else {
                ArrayList<String> t = new ArrayList<>();
                t.add(word);
                lettersToWord.put(sortedWord,t);
            }

            int word_len = sortedWord.length();

            if (sizeToWords.containsKey(word_len))
                sizeToWords.get(word_len).add(word);
            else {
                ArrayList<String> t = new ArrayList<>();
                t.add(word);
                sizeToWords.put(word_len, t);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        String str = sortLetters(targetWord);
        for (String i : wordList) {
            if (str.equals(sortLetters(i)))
                result.add(i);
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String str;
        for (char i = 'a'; i <= 'z'; i++) {
            str = sortLetters(word.concat("" + i));

            if (lettersToWord.containsKey(str))
                result.addAll(lettersToWord.get(str));
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> t = sizeToWords.get(wordLength);
        if (wordLength < MAX_WORD_LENGTH)
            wordLength++;

        while (true) {
            String s = t.get(random.nextInt(t.size()));
            if (getAnagramsWithOneMoreLetter(s).size() >= MIN_NUM_ANAGRAMS)
                return s;
        }
    }

    public String sortLetters(String word) {
        char[] char_words = word.toCharArray();
        Arrays.sort(char_words);
        return new String(char_words);
    }
}
