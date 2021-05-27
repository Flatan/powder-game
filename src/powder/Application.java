package powder;

import java.awt.EventQueue;
import javax.swing.JFrame;

<<<<<<< HEAD
// Test comment

=======
//Better test comment
>>>>>>> 4b86fe9c6c9e78103de7141dae38fa5cbf4d7c05
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
