package org.sosylab;

import java.io.IOException;

/**
 * Main class of the Wordle project. It starts the application to let the user play the game.
 *
 *
 */
public class WordleMain {

  /**
   * Launch the Wordle application.
   */
  public static void main(String[] args) throws IOException {
    new Shell().run();
  }

}
