package core;

import java.awt.EventQueue;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import javax.swing.JFrame;

import powder.ParticleGrid;

// Test comment

//Better test comment
/**
 * Application
 *
 * This class is the main entry point of the application
 *
 */
public class Application extends JFrame {

  public static int scale = 1;
  private static int WinH = 600;
  private static int WinW = 800;

  public static final Board board = new Board(WinW, WinH);
  public static final ParticleGrid grid = new ParticleGrid(WinW - 200, WinH);

  private Application() {
    initUI();
  }

  /**
   * Adds the JPanel board to the JFrame application and sets some configuration
   * settings
   */
  private void initUI() {
    add(board);
    setResizable(false);
    pack();

    setTitle("Powder Game");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

  /**
   * Entry point
   * 
   * @param args
   */
  public static void main(String[] args) {

    EventQueue.invokeLater(() -> {
      JFrame ex = new Application();
      ex.setVisible(true);
    });
  }
}
