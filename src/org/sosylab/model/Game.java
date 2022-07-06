package org.sosylab.model;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Manages a game of Wordle.
 */
public class Game {

  public static final int NUMBER_OF_GUESSES = 6;

  public static final int NUMBER_OF_CHARS_IN_WORD = 5;
  private final GameState state;
  private final List<Guess> guesses = new ArrayList<Guess>();
  private final Keyboard keyboard = new Keyboard();


  /**
   *
   * @param solution
   * @throws IOException
   */
  public Game(String solution) throws IOException {
    if (solution == "") {
      solution = WordProvider.drawRandomWord();
    }
    //System.out.println("Started game with: " + solution);
    state = new GameState(solution, NUMBER_OF_GUESSES);

    // So, lets start showing our play ground:
    showPlayground();

    while (!(state.getRemainingGuesses() == 0) && (state.getCurrentPhase() == Phase.RUNNING)) {
      //System.out.println("state.getRemainingGuesses():");
      //System.out.println(state.getRemainingGuesses());

      // Take in a word to be my Guess
      BufferedReader myG = new BufferedReader(new InputStreamReader(System.in));
      Optional<String> optMyGuess_asString = takeInGuess(myG);
      //System.out.println("myGuess: "+myGuess_asString);

      // I create my Guess
      if (optMyGuess_asString.isPresent()) {
        Optional<Guess> optG = guessWord(optMyGuess_asString.get());
        if (optG.isPresent()) {
          Guess myGuess = optG.get();

          guesses.add(myGuess);
          state.decreaseRoundCount();
          printRowWithHints(guesses);
          printRowsForRemainingGuesses(myGuess);


          // Updating the Keyboard
          keyboard.updateKeyboard(myGuess);
          //System.out.println("upgedatetes Keyboard?");
          System.out.println(getKeyboard().toString());

          if (myGuess.isWinner()) {
            state.setGameWon();
            System.out.println("Congratulations, you win!");
          }

          //GameState state_new = GameState(state);

        }

      } else {
        //System.out.println("Diese Meldung kommt nur bei FORFEIT!");
      }



    } // When it stops --> state.setGameLost();

  }

  /**
   * Throw an {@link IllegalArgumentException} if the words has more or less numbers than required.
   *
   * @param numberChars The amount of characters for the word that is to be checked.
   */
  static void throwErrorIfInvalidWordSize(int numberChars) {
    if (numberChars != Game.NUMBER_OF_CHARS_IN_WORD) {
      throw new IllegalArgumentException(
          "Guessed word must consist of  exactly " + Game.NUMBER_OF_CHARS_IN_WORD + " characters");
    }
  }

  /**
   * Get the keyboard.
   *
   * @return the keyboard
   */
  public Keyboard getKeyboard() {
    return keyboard;
  }

  /**
   * Check whether the game is won.
   *
   * @return true if the game is won.
   */
  public boolean isGameWon() {
    return guesses.get(guesses.size() - 1).isWinner();
  }

  /**
   * Make a guess.
   *
   * @param word the word that was guessed.
   * @return a Guess instance if the guess was legal
   */
  public Optional<Guess> guessWord(String word) {
    if (WordProvider.isValidWord(word)) {
      // dann gebe ich einen Guess zurück
      if (state.getCurrentPhase() == Phase.RUNNING) {
        SolutionWord mySol = state.getSolutionWord();
        Guess optG = mySol.guessWord(word);

        //for (int i = 0; i < 5; i++) {
        //  System.out.println(optG.getResults()[i]);
        //}
        return Optional.of(optG);
      } else {
        System.out.println("Error! No active game!");
      }
    } else {
      System.out.println("Error! Invalid guess!");
    }
    return Optional.empty();


  }

  /**
   * Get the list of guesses made by an user.
   *
   * @return the guesses made
   */
  public List<Guess> getUserGuesses() {
    return new ArrayList<>(guesses);
  }

  /**
   * Forfeit the game.
   */
  public void forfeit() {
    state.abortGame();
  }

  /**
   * Get the current game state.
   *
   * @return the game state
   */
  public GameState getState() {
    return state.createCopy();
  }

  /**
   * Method to show my Playground
   */
  private void showPlayground() {
    // my lines
    for (int i = 0; i < state.getRemainingGuesses(); i++) {
      System.out.println(" _  _  _  _  _  ");
    }
    // my keyboard
    System.out.println(keyboard.toString());
  }

  private Optional<String> takeInGuess(BufferedReader stdin) throws IOException {
    boolean quit = false;
    boolean validGuess = false;
    while (!quit && !validGuess) {
      quit = false;
      validGuess = false;
      System.out.print("Wordle> ");
      String input = stdin.readLine();
      writeToTestsFile(input);
      Scanner scanner = new Scanner(input);

      if (scanner.hasNext()) {
        // command is my first combination of letters
        String command = scanner.next();

        // so that both (lower and upper case letters work)
        command = command.toUpperCase();

        switch (command) {
          case "G":
            // Case I:  with solution word
            // Case II: without solution word
            if (scanner.hasNext()) {
              String myWord = scanner.next();
              char[] myWordAsChars = myWord.toCharArray();
              //System.out.println("myWordAsChars: " + myWordAsChars);
              if (myWordAsChars.length == 5) {
                if (scanner.hasNext()) {
                  System.out.println("Error! Too many arguments for command \"GUESS\"");
                } else {
                  //System.out.println("Ok, richtige Anzahl an Buchstaben");
                  validGuess = true;
                  return Optional.of(myWord);
                }

              } else {
                System.out.println("Error! Invalid guess!");
              }
            } else {
              System.out.println("Error! No guess provided!");
            }
            break;

          case "FORFEIT":
            forfeit();
            quit = true;
            break;

          case "NEW":
            // tbd!
            System.out.println("Error! Game already active!");
            break;

          case "QUIT":
            if (scanner.hasNext()) {
              System.out.println("Error! Too many arguments for Command \"QUIT\"");
            } else {
              System.out.println("Ok, quit game.");
              state.abortGame();
              quit = true;
            }
            break;

          default:
            System.out.println("Error! Command not found!");
            break;
        }
      } else {
        System.out.println("Error! Command not found!");
      }
      scanner.close();

    }
    return Optional.empty();
  }

  /**
   * @param myInput - Input from takeInGuess-Method will be written to Tests.txt.
   */

  private static void writeToTestsFile(String myInput) {
    try {
      // Creates a FileWriter
      FileWriter file = new FileWriter("src\\resources\\Tests.txt", true);

      // Creates a BufferedWriter
      BufferedWriter output = new BufferedWriter(file);

      // Makes new Line
      myInput = myInput + "\n";

      // Writes the string to the file
      output.write(myInput);

      // Closes the writer
      output.close();
    } catch (Exception e) {
      e.getStackTrace();
    }
  }

  private void printRowWithHints(List<Guess> myGuessList) {
    var currentGuess = myGuessList.get(0);
    for (int guess = 0; guess < myGuessList.size(); guess++) {
      currentGuess = myGuessList.get(guess);
      for (int i = 0; i < NUMBER_OF_CHARS_IN_WORD; i++) {
        if (currentGuess.getResults()[i] == GuessResult.WRONG) {
          System.out.print(currentGuess.getGuessedWord()[i] + "  ");
        }
        if (currentGuess.getResults()[i] == GuessResult.CONTAINED) {
          System.out.print("(" + currentGuess.getGuessedWord()[i] + ")  ");
        }
        if (currentGuess.getResults()[i] == GuessResult.CORRECT) {
          System.out.print("[" + currentGuess.getGuessedWord()[i] + "]  ");
        }
      }

      System.out.println("");

    }


  }

  private void printRowsForRemainingGuesses(Guess myGuess) {
    // remaining rows
    for (int i = 0; i < state.getRemainingGuesses(); i++) {
      System.out.println(" _   _   _   _   _  ");
    }
  }

}
