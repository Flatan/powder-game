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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import color.ColorGradientMap;

/**
 * Board
 *
 * Displays BufferedImage which is the main visual component
 */

public class Board extends JPanel implements Runnable {

    public static int runtimeParticleCount = 0;
    // Test comment 2

    private enum EventType {
        PARTICLE, FORCE
    }

    private EventType currentEventT = EventType.PARTICLE;

    private double scale = 1;
    // Width and height of the grid
    private int B_WIDTH = 600;
    private int B_HEIGHT = 600;

    // Milliseconds per frame:
    private int DELAY = 25;

    // Measures the framerate
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
    private double selectedTemp = 50;
    private int[] bgGrid;

    public Board() {

        initBoard();
    }

    // Setup initial settings and event listeners
    private void initBoard() {
        resetBoard();

        Particle.heatmap.addColor(0, Color.GREEN);
        Particle.heatmap.addColor(50, Color.YELLOW);
        Particle.heatmap.addColor(100, Color.RED);

        setFocusable(true);
        this.addKeyListener(ka);

        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                cursorSize -= 2 * e.getWheelRotation();

            }
        });

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

    private void resetBoard() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension((int) (B_WIDTH * scale), (int) (B_HEIGHT * scale)));

        ParticleGrid grid = new ParticleGrid(new Particle[B_WIDTH][B_HEIGHT]);
        Particle.grid = grid;
        image = new BufferedImage(B_WIDTH, B_HEIGHT, BufferedImage.TYPE_INT_RGB);
        bgGrid = new int[B_WIDTH * B_HEIGHT];
    }

    class KeyAction implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        // Dispatch key presses based on state
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyChar()) {
            case 'p':
                selectedElement = Granular.class;
                selectedColor = Color.white;
                break;
            case 's':
                selectedElement = Solid.class;
                selectedColor = Color.gray;
                break;
            case 't':
                if (Particle.showHeatMap)
                    Particle.showHeatMap = false;
                else
                    Particle.showHeatMap = true;
                break;
            case 'c':
                selectedTemp = 0;
                break;
            case 'w':
                selectedTemp = 50;
                break;
            case 'h':
                selectedTemp = 100;
                break;
            case '0':
                scale = 60;
                B_WIDTH = 10;
                B_HEIGHT = 10;
                DELAY = 100;
                resetBoard();
                break;
            case '1':
            	Particle.gravity = -0.5;
                scale = 2;
                B_WIDTH = 300;
                B_HEIGHT = 300;
                DELAY = 25;
                resetBoard();
                break;
            case '2':
            	Particle.gravity = -0.5;
                scale = 1;
                B_WIDTH = 600;
                B_HEIGHT = 600;
                DELAY = 25;
                resetBoard();
                break;
            case ' ':
                testCollison();
                
                break;
            // case 't':
            // selectedElement = Wind.class;
            // selectedColor = Color.blue;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }
    
    private void testCollison() {
    	Particle.gravity = 0;
    	Granular p1 = (Granular) spawnParticle(0,5,Color.WHITE,Granular.class);
    	p1.velX = 0.1;
    	Granular p2 = (Granular) spawnParticle(9,5,Color.WHITE,Granular.class);
    	p2.velX = -0.1;
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
        g2.scale(2, 2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        // The state of the game grid is drawn to an image buffer,
        // which is then drawn to the screen

        AffineTransform transform = new AffineTransform();
        transform.scale(scale, scale);
        g2.setTransform(transform);
        g2.drawImage(image, 0, 0, null);
        g2.drawOval(mouseX - cursorSize / 2, mouseY - cursorSize / 2, cursorSize, cursorSize);
        transform.setToIdentity();

        g2.setTransform(transform);

        g2.drawString("Hotkeys:", 0, 20);
        g2.drawString("p - powder", 0, 40);
        g2.drawString("s - solid", 0, 60);
        g2.drawString("t - toggle heat map display", 0, 80);
        g2.drawString("c - cold particles", 0, 100);
        g2.drawString("w - warm particles", 0, 120);
        g2.drawString("h - hot particles", 0, 140);
        g2.drawString("1 - low resolution", 0, 160);
        g2.drawString("2 - high resolution", 0, 180);

        if (fps < 40)
            g2.setColor(Color.red);
        else
            g2.setColor(Color.white);
        g2.drawString(String.format("FPS: %.2f", fps), B_WIDTH * (int) scale - 100, 20);
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
            Constructor<?> cons = elementType.getDeclaredConstructor(int.class, int.class, Color.class);
            particle = (Particle) cons.newInstance(x, y, color);
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
                            Particle p = spawnParticle(x, y, selectedColor, selectedElement);
                            p.temperature = selectedTemp;
                        }
            }
        }
    }

    /**
     * Writes the state of the ParticleGrid to the BufferedImage
     */
    private void updateBufferedImage() {

        ParticleGrid grid = Particle.getGrid();

        grid.updateParticles();

        image.setRGB(0, 0, B_WIDTH, B_HEIGHT, bgGrid, 0, 0);

        try {
            grid.forEachParticle((particle) -> {
                image.setRGB(particle.X(), B_HEIGHT - 1 - particle.Y(), particle.displayColor.getRGB());
            });
        } catch (ArrayIndexOutOfBoundsException e) {
        }

    }

    /**
     * Executes appropriate screen event based on the state of currentEventT
     * 
     * @param type Type of event
     */
    private void dispatchEvent(EventType type) {

        if (mouseDown)
            switch (type) {
            case PARTICLE:
                paintParticleCluster(mouseX, mouseY, cursorSize);
                break;

            case FORCE:

            default:
                break;
            }
    }

    /**
     * Collect the current mouse coordinates on the window
     */
    private void getMouseInfo() {

        try {
            mouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
        } catch (IllegalComponentStateException e2) {
        }

        try {
            mouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;
        } catch (IllegalComponentStateException e2) {
        }

        mouseX /= scale;
        mouseY /= scale;

    }

    // Copied this from a tutorial and don't know what it does; don't mess with it:
    // apparently a better way of making a game loop timer
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            getMouseInfo();
            dispatchEvent(currentEventT);
            updateBufferedImage();
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
