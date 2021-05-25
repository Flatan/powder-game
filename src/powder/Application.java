package powder;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Application extends JFrame {

    public Application() {

        initUI();
    }
    
    private void initUI() {
        
        add(new Board());

        setResizable(false);
        pack();
        
        setTitle("Powder Game");    
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Application();
            ex.setVisible(true);
        });
    }
}
