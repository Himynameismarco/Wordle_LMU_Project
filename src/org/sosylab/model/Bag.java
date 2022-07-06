package org.sosylab.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A multiset class. Allows to store duplicates within a set.
 *
 * @param <T> the element type
 */
class Bag<T> {

  private final Map<T, Integer> storage;

  /**
   * Create an empty new bag.
   */
  Bag() {
    storage = new HashMap<>();
  }

  /**
   * Create a new bag from an iterable.
   *
   * @param iter the source of elements
   */
  Bag(Iterable<T> iter) {
    this();
    for (T e : iter) {
      storage.compute(e, (k, v) -> (v == null) ? 1 : v + 1);
    }
  }

  /**
   * Remove an element from the bag.
   *
   * @param e the element to be removed
   * @return whether the element was present.
   */
  boolean removeElementIfPresent(T e) {
    Integer count = storage.get(e);

    if (count == null) {
      return false;
    }

    if (count - 1 == 0) {
      storage.remove(e);
    } else {
      storage.put(e, count - 1);
    }
    return true;
  }

}
