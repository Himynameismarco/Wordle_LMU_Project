package org.sosylab.model;

/**
 * The game state contains the data specific to a single Wordle game.
 */
public class GameState {

  private final SolutionWord solutionWord;
  private final Countdown countdown;

  private Phase phase;

  /**
   * Initialize the game state with a solution word that has to be guessed and a number of guesses
   * that a player has.
   *
   * @param wordToGuess     the word that should be guessed.
   * @param numberOfGuesses the number of guesses the player has.
   */
  GameState(final String wordToGuess, int numberOfGuesses) {
    solutionWord = new SolutionWord(wordToGuess);
    countdown = new Countdown(numberOfGuesses);

    phase = Phase.RUNNING;
  }

  /**
   * Initialize a game state using the values of an already existing state.
   *
   * @param state the existing state whose values are overtaken.
   */
  private GameState(GameState state) {
    solutionWord = state.solutionWord;
    countdown = state.countdown;
    phase = state.phase;
  }

  /**
   * Create a copy of this state that can be safely used for references.
   *
   * @return a copy of this state
   */
  GameState createCopy() {
    return new GameState(this);
  }

  /**
   * Get the solution word that is to be guessed.
   *
   * @return the word to guess
   */
  SolutionWord getSolutionWord() {
    return solutionWord;
  }

  /**
   * Get the current {@link Phase} of the game.
   *
   * @return the phase
   */
  public Phase getCurrentPhase() {
    return phase;
  }

  /**
   * Set the state of the game to {@link Phase#WON}.
   */
  void setGameWon() {
    phase = Phase.WON;
  }

  /**
   * Get the remaining guesses the player has.
   *
   * @return the remaining guesses
   */
  public int getRemainingGuesses() {
    return countdown.getValue();
  }

  /**
   * Forfeit the game.
   */
  void abortGame() {
    countdown.setToZero();
    setGameLost();
  }

  /**
   * Alert that a guess has been made.
   */
  void decreaseRoundCount() {
    countdown.decrease();
  }

  /**
   * Set the state of the game to {@link Phase#LOST}.
   */
  void setGameLost() {
    phase = Phase.LOST;
  }
}
