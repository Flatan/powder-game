package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class Keyboard implements KeyListener {

  private char lastChar = Character.MIN_VALUE;
  private HashSet<Character> uniqueKeys = new HashSet<Character>();
  public Character instance = null;

  protected Keyboard() {

  }

  /**
   * Tests if a given key is currently toggled
   * 
   * @param c the character to test against
   * @return boolean true if toggled false if not
   */
  public boolean keyToggled(char c) {

    if (uniqueKeys.contains(c)) {
      return true;
    }
    return false;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {

    this.lastChar = e.getKeyChar();

    // System.out.println(e.getKeyCode());

    if (uniqueKeys.contains(lastChar)) {
      uniqueKeys.remove(lastChar);
    } else {
      uniqueKeys.add(lastChar);
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

}
