package powder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;

import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Board
 *
 * Displays BufferedImage which is the main visual component
 */

public class Board extends JPanel implements Runnable {

    public static int runtimeParticleCount = 0;
    // Test comment 2

    // Width and height of the window
    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;

    // Milliseconds per frame:
    private final int DELAY = 25;
    
    //Measures the framerate
    private double fps = 0;

    // Mouse conditions
    private int mouseX, mouseY;
    private int prevX, prevY;
    private boolean mouseDown = false;

    // Defines the area of powder placement
    private int cursorSize = 20;
    
    private KeyAction ka = new KeyAction();

    private BufferedImage image;
    private Thread animator;
    private Particle p = null;
    private Class<? extends Particle> selectedElement = Granular.class;
    private Color selectedColor = Color.white;

    public Board() {

        initBoard();
    }

    // Setup initial settings and event listeners
    private void initBoard() {
    	setFocusable(true);
    	this.addKeyListener(ka);
    	
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        ParticleGrid grid = new ParticleGrid(new Particle[B_WIDTH][B_HEIGHT]);
        image = new BufferedImage(B_WIDTH, B_HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				cursorSize -= 2*e.getWheelRotation();
				
			}});
        
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
    
    class KeyAction implements KeyListener{
		@Override
        public void keyTyped(KeyEvent e) {
        }

        //Dispatch key presses based on state
		@Override
		public void keyPressed(KeyEvent e) {
        
			switch(e.getKeyChar()) {
				case 'p':
					selectedElement = Granular.class;
					selectedColor = Color.white;
					break;
				case 's':
					selectedElement = Particle.class;
					selectedColor = Color.gray;
					break;
				}
		}

		@Override
		public void keyReleased(KeyEvent e) {}
		
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
        
        g2.drawString("Hotkeys:", 0, 20);
        g2.drawString("p - powder", 0, 40);
        g2.drawString("s - solid", 0, 60);
        
        
        if (fps<40)
        	g2.setColor(Color.red);
        else
        	g2.setColor(Color.white);
        g2.drawString(String.format("FPS: %.2f", fps), 500, 20);
    }

    /**
     * Convenience method to initialize a new particle and add it to the cartesian
     * grid
     * 
     * @param x X coordinate
     * @param y Y coordinate
     */
    private Particle spawnParticle(int x, int y, Color color, Class<? extends Particle> elementType) {

        ParticleGrid grid = Particle.getGrid();

        Particle particle = null;
        try {
        	 Constructor<?> cons = elementType.getDeclaredConstructor(int.class,int.class,Color.class);
			 particle = (Particle) cons.newInstance(x,y,color);
		} catch (Throwable e) {
			System.out.println(e);
		}
        grid.set(x, y, particle);
        return particle;
    }

    // Draw a cluster of particles on the screen given (x, y) coords and a diameter
    public void paintParticleCluster(int mx, int my, int diameter) {

        ParticleGrid grid = Particle.getGrid();

        my = B_HEIGHT - 1 - my;

        if (prevX != mx && prevY != my) {
            Logger.log("particleCluster (%d,%d)", mx, my);
        }

        prevX = mx;
        prevY = my;

        for (int x = mx - diameter / 2; x < mx + diameter / 2; x++) {
            for (int y = my - diameter / 2; y < my + diameter / 2; y++) {

                if (!grid.outOfBounds(x, y))
                if (grid.get(x, y) == null)
                if (Math.hypot(x - mx, y - my) <= diameter / 2) {
                	spawnParticle(x,y,selectedColor,selectedElement);
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

        ParticleGrid grid = Particle.getGrid();

        if (mouseDown)
            paintParticleCluster(mouseX, mouseY, cursorSize);

        Particle.updateGrid();

        for (int x = 0; x < B_WIDTH; x++) {
            for (int y = B_HEIGHT - 1; y >= 0; y--) {
                if (grid.get(x, y) != null)
                    image.setRGB(x, B_HEIGHT - 1 - y, grid.get(x, y).color.getRGB());
                else
                    image.setRGB(x, B_HEIGHT - 1 - y, Color.BLACK.getRGB());
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
            
            fps = 1/((sleep+timeDiff)*1e-3);

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
