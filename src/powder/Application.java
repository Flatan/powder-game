package powder;

import java.awt.EventQueue;
import javax.swing.JFrame;


// Test comment

//Better test comment
/**
 * Application 
 *
 * This class is the main entry point of the application
 *
 */
public class Application extends JFrame {

    public Application() {
        initUI();
    }
    
    // Initialize a board object and set some window settings
    private void initUI() {
        
        add(new Board());

        setResizable(false);
        pack();
        
        setTitle("Powder Game");    
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
    }
    

    // Application entry point
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Application();
            ex.setVisible(true);
        });
    }
}
