package org.sosylab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import org.sosylab.model.Game;

public class Shell {

  private static final String PROMPT = "Wordle> ";

  /**
   * The shell waits for the user to enter some input.
   * At this point, a user is only able to create a new game using the NEW command.
   * For debugging and testing purposes, a user shall additionally have the possibility to
   * create a new game using a predefined word by adding an optional parameter to the NEW command.
   *
   */
  public void run() throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    startGameOrQuit(stdin);

  }


  /**
   *
   * @param stdin - my BufferedReader taking in user Input
   * @throws IOException - because UserInput
   */

  private static void startGameOrQuit(BufferedReader stdin) throws IOException {
    boolean validSolution = false;
    boolean quit = false;
    while (!validSolution && !quit) {
      quit = false;
      validSolution = false;
      System.out.print(PROMPT);
      String input = stdin.readLine();
      writeToTestsFile(input);
      Scanner scanner = new Scanner(input);
      scanner.useDelimiter("\\s+");
      if (scanner.hasNext()) {
        // command is my first combination of letters
        String command = scanner.next();

        // so that both (lower and upper case letters work)
        command = command.toUpperCase();

        switch (command) {
          case "NEW":
            // with solution word
            if (scanner.hasNext()) {
              String myWord = scanner.next();
              if (scanner.hasNext()) {
                System.out.println("Error! Too many arguments for command \"NEW\"");
              } else {
                if (myWord.length() == 5) {
                  System.out.println("mySolution: " + myWord);
                  validSolution = true;
                  Game myG = new Game(myWord);

                } else {
                  System.out.println("Error! Invalid word to guess!");
                }
              }
            } else {
              // without solution word
              String myWord = "";
              Game myG = new Game(myWord);
              validSolution = true;
            }
            break;

          case "QUIT":
            if (scanner.hasNext()){
              System.out.println("Error! Too many arguments for Command \"QUIT\"");
              break;
            } else {
              System.out.println("Quit game.");
              quit = true;
              break;
            }

          default:
            System.out.println("Error! Command not found!");
            break;
        }
      } else {
        System.out.println("Error! Command not found!");
      }
      scanner.close();

    }
  }

  /**
   * write input to Tests.txt
   *
   * @param myInput - Input from startGameOrQuit-Method
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



}
