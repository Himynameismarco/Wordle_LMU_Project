package org.sosylab.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The word provider loads and manages the set of allowed guessing words.
 */
class WordProvider {
  private static final String SOLUTION_WORDLE_FILE_PATH = "wordle_solutions.txt";
  private static final String ACCEPT_WORDLE_FILE_PATH = "wordle_allowed.txt";

  private static final Set<String> SOLUTION_WORDS;
  private static final Set<String> ALL_WORDS;

  private static final Random RANDOM = new Random();

  static {
    SOLUTION_WORDS = loadWordsFromFile(SOLUTION_WORDLE_FILE_PATH);

    ALL_WORDS = loadWordsFromFile(ACCEPT_WORDLE_FILE_PATH);
    ALL_WORDS.addAll(SOLUTION_WORDS);
  }

  /**
   * Load a file from a given name and store the content in a unique set  of words.
   *
   * @param filename the path to the file relative to the resource directory
   * @return a set containing a unique list of all words found in the file.
   */
  private static Set<String> loadWordsFromFile(final String filename) {

    InputStream inputStream = WordProvider.class.getClassLoader().getResourceAsStream(filename);
    if (inputStream == null) {
      throw new AssertionError("Inputstream may not be null");
    }

    try (
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader)) {

      return reader.lines().collect(Collectors.toCollection(HashSet::new));

    } catch (IOException e) {
      // An exception due to IO should not occur at this point. It is probably a bug in the
      // implementation that needs to be fixed
      throw new AssertionError(e);
    }
  }

  /**
   * Draw a word from the list of possible solutions.
   *
   * @return a word to guess if the list of possible solutions is not empty
   */
  static String drawRandomWord() {
    int size = SOLUTION_WORDS.size();
    return SOLUTION_WORDS.stream().skip(RANDOM.nextInt(size)).findFirst().orElseThrow();
  }

  /**
   * Check whether a word is a valid guess.
   *
   * @param word the word to check
   * @return true if it is a valid guess.
   */
  static boolean isValidWord(String word) {
    return ALL_WORDS.contains(word.toLowerCase());
  }
}
