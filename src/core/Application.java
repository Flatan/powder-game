package core;

import java.awt.EventQueue;
import javax.swing.JFrame;

import math.Vector2D;

// Test comment

//Better test comment
/**
 * Application
 *
 * This class is the main entry point of the application
 *
 */
public class Application extends JFrame {

    private static Board board = new Board();

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
     * Returns the main application board
     * 
     * @return
     */
    public static Board getBoard() {
        return board;
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
