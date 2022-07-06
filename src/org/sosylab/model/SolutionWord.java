package org.sosylab.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The solution word contains all information how an arbitrary guess relates to the solution word.
 */
class SolutionWord {

  private final String completeWord;
  private final List<Character> slots;

  /**
   * Create a new word to guess.
   *
   * @param solutionWord the chosen word to guess
   */
  SolutionWord(String solutionWord) {
    requireNonNull(solutionWord);
    completeWord = solutionWord.toLowerCase();

    char[] chars = completeWord.toCharArray();
    slots = new ArrayList<>(Game.NUMBER_OF_CHARS_IN_WORD);
    for (char c : chars) {
      slots.add(c);
    }
  }

  /**
   * Compare a guessed word against this word.
   *
   * @param word the guess made
   * @return the evaluation of the guess
   */
  Guess guessWord(String word) {
    requireNonNull(word);

    char[] guess = word.toLowerCase().toCharArray();
    Bag<Character> unusedLetters = new Bag<>(slots);
    GuessResult[] result = new GuessResult[Game.NUMBER_OF_CHARS_IN_WORD];

    for (int i = 0; i < Game.NUMBER_OF_CHARS_IN_WORD; i++) {
      if (slots.get(i) == guess[i]) {
        result[i] = GuessResult.CORRECT;
        unusedLetters.removeElementIfPresent(slots.get(i));
      }
    }

    for (int i = 0; i < Game.NUMBER_OF_CHARS_IN_WORD; i++) {
      if (result[i] != null) {
        continue;
      }

      if (unusedLetters.removeElementIfPresent(guess[i])) {
        result[i] = GuessResult.CONTAINED;
      } else {
        result[i] = GuessResult.WRONG;
      }
    }

    return new Guess(guess, result);
  }

  /**
   * Reveal the solution word that is to be guessed.
   *
   * @return a guess of the word itself
   */
  Guess reveal() {
    GuessResult[] result = new GuessResult[Game.NUMBER_OF_CHARS_IN_WORD];
    Arrays.fill(result, GuessResult.CORRECT);
    return new Guess(completeWord.toCharArray(), result);
  }
}
