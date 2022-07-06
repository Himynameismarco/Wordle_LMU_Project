package org.sosylab.model;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * The keyboard shows a player the remaining available letters.
 * If a letter turns out to not be part of a word, it gets removed from the
 * keyboard.
 */
class Keyboard {

  private static final String[] INITIAL_LAYOUT = {
      "q,w,e,r,t,z,u,i,o,p",
      "a,s,d,f,g,h,j,k,l",
      "y,x,c,v,b,n,m"
  };

  private final Set<String> deactivatedLetters;

  Keyboard() {
    deactivatedLetters = new HashSet<>();
  }

  /**
   * Update the remaining letters after a guess.
   *
   * @param guess the guess to be used for the update
   */
  void updateKeyboard(Guess guess) {
    requireNonNull(guess);
    Game.throwErrorIfInvalidWordSize(guess.getGuessedWord().length);

    Set<String> lettersToRemove = new HashSet<>();
    for (int i = 0; i < Game.NUMBER_OF_CHARS_IN_WORD; i++) {
      if (guess.getResults()[i] == GuessResult.WRONG) {
        lettersToRemove.add(String.valueOf(guess.getGuessedWord()[i]));
      }
    }

    for (String row : INITIAL_LAYOUT) {
      String[] letters = row.split(",");
      for (String letter : letters) {
        if (lettersToRemove.contains(letter)) {
          deactivatedLetters.add(letter);
        }
      }
    }
  }

  @Override
  public String toString() {
    String result = "";
    System.out.println("");
    for (String row : INITIAL_LAYOUT) {
      String[] letters = row.split(",");
      //System.out.println("letters: "+letters);
      for (String letter : letters) {
        if (deactivatedLetters.contains(letter)) {
          result = result + "   ";
        } else {
          result = result + " " + letter + " ";
          //System.out.print(letter+" ");
        }
      }
      result = result + "  \n";
      //System.out.println("");
    }
    return result;
  }
}
