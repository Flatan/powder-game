package powder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;

import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Board
 *
 * Displays BufferedImage which is the main visual component
 */

public class Board extends JPanel implements Runnable {
	
	//Test comment 2

    // Width and height of the window
    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;

    // Milliseconds per frame:
    private final int DELAY = 25;

    
    // Mouse conditions
    private int mouseX, mouseY;
    private int prevX, prevY;
    private boolean mouseDown = false;

    // Defines the area of powder placement
    private int cursorSize = 20;

    private BufferedImage image;
    private Thread animator;
	private Particle p = null;

    public Board() {

        initBoard();
    }

    // Setup initial settings and event listeners
    private void initBoard() {

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        image = new BufferedImage(B_WIDTH, B_HEIGHT, BufferedImage.TYPE_INT_RGB);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Logger.log("mouseDown event");
                mouseDown = true;
            }

            public void mouseReleased(MouseEvent e) {
                Logger.log("mouseReleased event");
                mouseDown = false;
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    // Automatically called whenever the board is redrawn
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        // The state of the game grid is drawn to an image buffer,
        // which is then drawn to the screen
        g2.drawImage(image, 0, 0, null);
        g2.drawOval(mouseX - cursorSize / 2, mouseY - cursorSize / 2, cursorSize, cursorSize);
    }

    // For testing if a single (x,y) coord fits on the screen
    public boolean outOfBounds(int x, int y) {

        return !(x < B_WIDTH && y < B_HEIGHT && x > 0 && y > 0);

    }

    // Draw a cluster of particles on the screen given (x, y) coords and a diameter
    public void paintParticleCluster(int mx, int my, int diameter) {

        CartesianGrid<Particle> grid = Particle.getGrid();

        my = B_HEIGHT - 1 - my;

        if (prevX != mx && prevY != my) {
            Logger.log("particleCluster (%d,%d)", mx, my);
        }

        prevX = mx;
        prevY = my;

        for (int x = mx - diameter / 2; x < mx + diameter / 2; x++) {
        for (int y = my - diameter / 2; y < my + diameter / 2; y++) {

                if (!outOfBounds(x, y))
                if (grid.get(x, y) == null)
                if (Math.hypot(x - mx, y - my) <= diameter / 2) {
                            new Granular(x, y, Color.WHITE);
                        }
            }
        }
    }

    private void cycle() {
        try {
            mouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
        } catch (IllegalComponentStateException e2) {
        }

        try {
            mouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;
        } catch (IllegalComponentStateException e2) {
        }

        CartesianGrid<Particle> grid = Particle.getGrid();
        

       /*for(int x = 0; x<B_WIDTH; x++) {
       	   new Particle(x,(int) (B_HEIGHT-50*Math.sin(x/50.)-300),Color.RED);
       }*/
        
        
        /*if (mouseDown)
        	p  = new Particle(mouseX, mouseY);
        if (p != null)
        	System.out.println(p.slope());
        */
        
        
        if (mouseDown)
        	paintParticleCluster(mouseX, mouseY, cursorSize);

        Particle.updateGrid();

        // Convert the Particle[][] grid to an actual buffered image
        for (int x = 0; x < B_WIDTH; x++) {
        for (int y = B_HEIGHT-1; y >= 0; y--) {
            if (grid.get(x, y) != null)
                    image.setRGB(x, B_HEIGHT-1-y, grid.get(x, y).color.getRGB());
            else
                    image.setRGB(x, B_HEIGHT-1-y, Color.BLACK.getRGB());
            }
        }
    }

    // Copied this from a tutorial and don't know what it does; don't mess with it:
    // apparently a better way of making a game loop timer
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            cycle();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

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
