package org.sosylab.model;

/**
 * A simple counter.
 */
class Countdown {

  private int counter;

  /**
   * Initialize countdown with a positive value.
   *
   * @param start the value where the countdown starts.
   */
  Countdown(int start) {
    if (start <= 0) {
      throw new AssertionError("The counter may not be initialized with a negative number!");
    }

    counter = start;
  }

  /**
   * Decrement the countdown.
   */
  void decrease() {
    if (counter <= 0) {
      throw new AssertionError("The counter cannot be decreased below zero!");
    }

    counter--;
  }

  /**
   * Return the current value of the countdown.
   */
  int getValue() {
    return counter;
  }

  /**
   * Set the countdown to zero.
   */
  void setToZero() {
    counter = 0;
  }

}
