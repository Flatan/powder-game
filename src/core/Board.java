package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.ArrayDeque;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.HashSet;

import ui.UIEvent;
import ui.UI;
import ui.UI.EventT;
import powder.*;

/**
 * Board
 *
 * The purpose of the board is to constantly update the buffered image and flush
 * the next frame to the screen. It passes around a Graphics2D object for other
 * classes to borrow every iteration but never draws anything independently.
 */

public class Board extends JPanel implements Runnable {

  public static int runtimeParticleCount = 0;

  private ArrayDeque<EventT> eventQueue = new ArrayDeque<>();
  public Object eventSignal = null;

  private int W;
  private int H;

  // Milliseconds per frame:
  private int DELAY = 25;

  // Measures the framerate
  private double fps = 0;

  private Thread animator;
  private ArrayList<UIEvent> UIevents = new ArrayList<UIEvent>();

  private UI ui;

  public Board(int W, int H) {
    this.W = W;
    this.H = H;

    initBoard();

  }

  // Setup initial settings and event listeners
  private void initBoard() {
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension((int) (W * Application.scale), (int) (H * Application.scale)));

    ui = new UI(this);

    Particle.heatmap.addColor(0, Color.GREEN);
    Particle.heatmap.addColor(50, Color.YELLOW);
    Particle.heatmap.addColor(100, Color.RED);

    Particle.slopemap.addColor(-5, Color.RED);
    Particle.slopemap.addColor(0, Color.WHITE);
    Particle.slopemap.addColor(5, Color.GREEN);

    setFocusable(true);
  }

  /**
   * Get the initialized UI events
   * 
   * @return
   */
  public ArrayList<UIEvent> getConnectedEvents() {
    return UIevents;
  }

  /**
   * Set the width of the board
   * 
   * @param w
   */
  public void setWidth(int w) {
    this.W = w;
  }

  /**
   * Set the height of the board
   * 
   * @param h
   */
  public void setHeight(int h) {
    this.H = h;
  }

  /**
   * Set the delay
   * 
   * @param delay
   */
  public void setDelay(int delay) {
    this.DELAY = delay;
  }

  /**
   * Get the FPS at this exact moment
   * 
   * @return
   */
  public double getFPS() {
    return fps;
  }

  public void connectEvent(Class<? extends UIEvent> event) {

    try {
      UIEvent instance = event.getDeclaredConstructor().newInstance();
      UIevents.add(instance);
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  @Override
  public void addNotify() {
    super.addNotify();
    animator = new Thread(this);
    animator.start();
  }

  /**
   * Automatically called whenever the board is redrawn
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
    g2.scale(2, 2);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(Color.WHITE);

    Application.grid.draw(g2);
    ui.draw(g2);
  }

  public void queueEvent(EventT arg) {
    eventQueue.add(arg);
  }

  // Copied this from a tutorial and don't know what it does; don't mess with it:
  // apparently a better way of making a game loop timer
  @Override
  public void run() {

    long beforeTime, timeDiff, sleep;

    HashSet<UIEvent> ActiveEventBuffer = new HashSet<UIEvent>();

    beforeTime = System.currentTimeMillis();

    while (true) {

      // Allows the KeyPressed() event to deliver the key only once
      // by immediately switching instanceBuffer back to "MIN_VALUE"
      // (char placeholder for "null")
      eventSignal = eventQueue.poll();

      // Stream each UIEvent if its ".trigger()" gate is open
      for (UIEvent E : UIevents) {
        if (E.trigger()) {

          if (!ActiveEventBuffer.contains(E)) {
            E.on(true);
            ActiveEventBuffer.add(E);
          } else {
            E.on(false);
          }
        } else {

          if (ActiveEventBuffer.contains(E)) {
            E.off(true);
            ActiveEventBuffer.remove(E);
          } else {
            E.off(false);
          }
        }
      }

      // The particles can't be updated from within the paintComponent method because
      // for some dumb reason repaint() doesn't wait to finish running before the
      // program continues on
      // and it breaks the framerate counter so I had to move updateParticles() out
      // here

      // Also potentially means the drawing of buffered image could be causing lag not
      // registered by
      // the counter but idk
      Application.grid.updateParticles();
      repaint();

      timeDiff = System.currentTimeMillis() - beforeTime;
      sleep = DELAY - timeDiff;

      if (sleep < 0) {
        sleep = 2;

      }

      fps = 1 / ((sleep + timeDiff) * 1e-3);

      try {
        Thread.sleep(sleep);
      } catch (InterruptedException e) {

        String msg = String.format("Thread interrupted: %s", e.getMessage());

        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
      }

      beforeTime = System.currentTimeMillis();
    }
  }
}
