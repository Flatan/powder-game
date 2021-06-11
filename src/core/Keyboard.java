package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Hashtable;
import core.UI.EventT;

public class Keyboard implements KeyListener {

  private char c = Character.MIN_VALUE;
  private HashSet<Character> toggleBuffer = new HashSet<Character>();
  private Hashtable<Character, Integer> countBuffer = new Hashtable<Character, Integer>();
  public Character instance = null;

  protected Keyboard() {

  }

  /**
   * Returns the last key pressed by the user. Will return Character.MIN_VALUE as
   * a placeholder for "null".
   * 
   * @return char
   */
  public char lastKeyPressed() {
    return c;
  }

  /**
   * Returns the key delivered by a key press event
   * 
   * @return c
   */
  public char keyPressed() {

    if (Application.board.eventSignal == EventT.KEYPRESS)
      return c;

    return Character.MIN_VALUE;

  }

  /**
   * Returns the number of times a key has been repeated in a row
   * 
   * @param c the character to check
   * @return int repeat count
   */

  public int keyRepeated(char c) {
    return countBuffer.getOrDefault(c, 0);
  }

  /**
   * Tests if a given key is currently toggled
   * 
   * @param c the character to test against
   * @return boolean true if toggled false if not
   */
  public boolean keyToggled(char c) {

    if (toggleBuffer.contains(c)) {
      return true;
    }
    return false;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {

    this.c = e.getKeyChar();

    if (countBuffer.containsKey(c)) {
      countBuffer.replace(c, countBuffer.get(c) + 1);
    } else {
      countBuffer.clear();
      countBuffer.put(c, 1);
    }

    if (toggleBuffer.contains(c)) {
      toggleBuffer.remove(c);
    } else {
      toggleBuffer.add(c);
    }

    Application.board.queueEvent(EventT.KEYPRESS);
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

}
