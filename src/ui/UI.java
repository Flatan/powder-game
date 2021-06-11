package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;

import core.Board;

/**
 * UI
 *
 * Class that acts like a "UI core" that dispatches events and also has a direct
 * line of communication with the main application core. Holds instances of
 * keyboard and mouse for easy access.
 */
public class UI {

  private Board B;

  HashSet<Integer> positionBuffer = new HashSet<Integer>();

  public static final Keyboard keyboard = new Keyboard();
  public static final Mouse mouse = new Mouse();

  public UI(Board B) {
    this.B = B;
    B.addKeyListener(UI.keyboard);
    B.addMouseWheelListener(UI.mouse.wheelControls);
    B.addMouseListener(UI.mouse.adapter);
    B.connectEvent(AlwaysOn.class);
    B.connectEvent(ParticleFactory.class);
    B.connectEvent(ShowHeatMap.class);
    B.connectEvent(Spinner.class);
    B.connectEvent(Resolution.class);
    B.connectEvent(Button.class);
    B.connectEvent(Logger.class);

  }

  /**
   * Simple tool for storing lines of text and drawing them
   */
  public class Printer {

    int vertSpacing;
    int x = 0;
    int y = 0;
    final int padX;
    final int padY;
    int fs;
    Graphics2D g;

    Printer(Graphics2D g, int vertSpacing, int padX, int padY) {
      this.vertSpacing = vertSpacing;
      this.g = g;
      this.padX = padX;
      this.padY = padY;
      this.fs = g.getFont().getSize();
    }

    public void setLocation(int x, int y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Draw the contents of the buffer to the internally stored Graphics2D object.
     * Draws from the upper left corner of the provided coordinates.
     * 
     * @param x
     * @param y
     */
    public void println(String str) {

      y += fs + vertSpacing;
      g.drawString(str, x + padX, y + padY);
    }

    public void println(String str, Color c) {

      g.setColor(c);
      this.y += fs + vertSpacing;
      g.drawString(str, x + padX, y + padY);
      g.setColor(Color.WHITE);
    }
  }

  /**
   * Iterates through every the .draw() method of every connected {@link UIEvent}.
   * It provides .draw() with the active Graphics2D object and a helper class for
   * drawing text {@link TextBuffer} (which also wraps the same Graphics2D). This
   * is the main dispatcher of the "UI core".
   * 
   * @param g2
   */
  public void draw(Graphics2D g2) {

    Printer p = new Printer(g2, 5, 5, 0);

    for (UIEvent event : B.getConnectedEvents()) {
      event.draw(p, g2);
    }

  }

}
