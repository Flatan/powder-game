package core;

import java.awt.Graphics2D;
import java.util.HashSet;

/**
 * UI
 *
 * Class that acts like a "UI core" that dispatches events and also has a direct
 * line of communication with the main application core. Holds instances of
 * keyboard and mouse for easy access.
 */
public class UI {

  HashSet<Integer> positionBuffer = new HashSet<Integer>();

  public static final Keyboard keyboard = new Keyboard();
  public static final Mouse mouse = new Mouse();

  public UI(Board B) {
    // B.addKeyListener(UI.keyboard);
  }

  /**
   * Iterates through every the .draw() method of every connected {@link UIEvent}.
   * It provides .draw() with the active Graphics2D object and a helper class for
   * drawing text {@link TextBuffer} (which also wraps the same Graphics2D). This
   * is the main dispatcher of the "UI core".
   * 
   * @param g2
   */
  protected void draw(Graphics2D g2) {

    UI.mouse.draw(g2);

  }
}
