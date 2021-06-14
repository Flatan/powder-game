package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.awt.geom.Ellipse2D;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import math.Vector2D;
import powder.*;
import ui.EventLoader;

/**
 * Board
 *
 * The purpose of the board is to constantly update the buffered image and flush
 * the next frame to the screen. It passes around a Graphics2D object for other
 * classes to borrow every iteration but never draws anything independently.
 */

public class Board extends JPanel implements Runnable {

  public static int runtimeParticleCount = 0;

  public Object eventSignal = null;

  private int W;
  private int H;

  // Milliseconds per frame:
  private int DELAY = 25;

  // Measures the framerate
  private double fps = 0;

  private List<String> commandClasses = new ArrayList<>();
  private Thread animator;
  private ArrayList<Command> commands = new ArrayList<>();

  private UI ui;

  public Board(int W, int H) {
    this.W = W;
    this.H = H;

    initBoard();

  }

  private void initCommands() {
    // https://stackoverflow.com/questions/34459486/joining-paths-in-java
    Path currentPath = Paths.get(System.getProperty("user.dir"));
    Path cmdPath = Paths.get(currentPath.toString(), "cmd");

    try (Stream<Path> walk = Files.walk(cmdPath)) {
      // https://mkyong.com/java/java-how-to-list-all-files-in-a-directory/
      List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".class")).collect(Collectors.toList());

      result.forEach(System.out::println);
      result.forEach((path) -> {
        Path classFile = Paths.get(path);
        String className = classFile.getFileName().toString().split("\\.")[0];
        commandClasses.add("cmd." + className);
      });

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Setup initial settings and event listeners
  private void initBoard() {
    initCommands();
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension((int) (W * Application.scale), (int) (H * Application.scale)));

    commandClasses.forEach((cmdstr) -> {
      try {
        Object obj = Class.forName(cmdstr).getDeclaredConstructor().newInstance();
        Command cmd = (Command) obj;
        connectCommand(cmd);
        System.out.println("Loaded " + cmdstr);
      } catch (Exception e) {
        e.printStackTrace();
      }

    });

    Particle.heatmap.addColor(0, Color.GREEN);
    Particle.heatmap.addColor(50, Color.YELLOW);
    Particle.heatmap.addColor(100, Color.RED);

    Particle.slopemap.addColor(-5, Color.RED);
    Particle.slopemap.addColor(0, Color.WHITE);
    Particle.slopemap.addColor(5, Color.GREEN);

    setFocusable(true);
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

  public void connectCommand(Command instance) {

    try {
      // Command instance = cmd.getDeclaredConstructor().newInstance();
      commands.add(instance);
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  public boolean testCommand(String cmd) {

    String[] arguments = cmd.split(" ");
    for (Command c : commands) {
      if (arguments[0].toLowerCase().equals(c.getClass().getSimpleName().toLowerCase())) {
        return true;
      }
    }
    return false;
  }

  public void invokeCommand(String cmd) {

    String[] arguments = cmd.split(" ");
    for (Command c : commands) {
      if (arguments[0].toLowerCase().equals(c.getClass().getSimpleName().toLowerCase())) {
        c.call(arguments);
      }
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

  // Copied this from a tutorial and don't know what it does; don't mess with it:
  // apparently a better way of making a game loop timer
  @Override
  public void run() {

    ui = new UI(this);

    addMouseWheelListener(UI.mouse.wheelControls);
    addMouseListener(UI.mouse.adapter);
    EventLoader.loadEvents(this);
    long beforeTime, timeDiff, sleep;

    UI.mouse.setShape(new Ellipse2D.Float(0, 0, 1, 1), true);
    Application.grid.reset(600, 600);
    // Application.grid.reset(600, 600);
    // Particle tracer = Application.grid.spawn(300, 300, Tracer.class);
    // tracer.vel = new Vector2D(2, 0);
    beforeTime = System.currentTimeMillis();

    while (true) {

      repaint();

      Application.grid.updateParticles();

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
