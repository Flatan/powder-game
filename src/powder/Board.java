package powder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import color.ColorGradientMap;
import ui.KeyAction;
import ui.Mouse;
import ui.Foreground;

/**
 * Board
 *
 * The purpose of the board is to constantly update the buffered image and flush
 * the next frame to the screen. It passes around a Graphics2D object for other
 * classes to borrow every iteration but never draws anything independently.
 */

// TODO ^^ Make this true

public class Board extends JPanel implements Runnable {

    public static int runtimeParticleCount = 0;
    // Test comment 2

    private double scale = 1;
    // Width and height of the grid
    private int B_WIDTH = 600;
    private int B_HEIGHT = 600;

    // Milliseconds per frame:
    private int DELAY = 25;

    // Measures the framerate
    private double fps = 0;

    // Mouse conditions
    private int prevX, prevY;
    private boolean mouseDown = false;

    // Defines the area of powder placement
    private int cursorSize = 20;

    private KeyAction ka = new KeyAction();

    // private BufferedImage image;
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
        reset();

        Particle.heatmap.addColor(0, Color.GREEN);
        Particle.heatmap.addColor(50, Color.YELLOW);
        Particle.heatmap.addColor(100, Color.RED);

        setFocusable(true);

        addKeyListener(ka);
        addMouseWheelListener(Mouse.wheelControls);

        addMouseListener(new MouseAdapter() {
            // TODO move to ui.Mouse
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

    /**
     * Set the current element
     * 
     * @param element
     */
    public void setSelectedElement(Class<? extends Particle> element) {
        this.selectedElement = element;
    }

    public void setCursorSize(int cursorSize) {
        // TODO move to ui.Mouse
        this.cursorSize = cursorSize;
    }

    public int getCursorSize() {
        // TODO move to ui.Mouse
        return cursorSize;
    }

    /**
     * Set the color of the current element
     * 
     * @param c
     */
    public void setSelectedColor(Color c) {
        this.selectedColor = c;
    }

    /**
     * Set the selected temp
     * 
     * @param temp
     */
    public void setSelectedTemp(double temp) {
        this.selectedTemp = temp;
    }

    /**
     * Set the current scale
     * 
     * @param s
     */
    public void setScale(double s) {
        this.scale = s;
    }

    /**
     * Get the current scale
     * 
     * @return
     */
    public double getScale() {
        return scale;
    }

    /**
     * Set the width of the board
     * 
     * @param w
     */
    public void setWidth(int w) {
        this.B_WIDTH = w;
    }

    /**
     * Set the height of the board
     * 
     * @param h
     */
    public void setHeight(int h) {
        this.B_HEIGHT = h;
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

    public void reset() {
        setBackground(Color.BLACK);
        Logger.log("Scale: %f", scale);
        setPreferredSize(new Dimension((int) (B_WIDTH * scale), (int) (B_HEIGHT * scale)));

        ParticleGrid grid = new ParticleGrid(new Particle[B_WIDTH][B_HEIGHT]);
        Particle.grid = grid;
        // image = new BufferedImage(B_WIDTH, B_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // bgGrid = new int[B_WIDTH * B_HEIGHT];
    }

    public void testCollison() {
        Particle.gravity = 0;
        Granular p1 = (Granular) spawnParticle(0, 5, Color.WHITE, Granular.class);
        p1.velX = 0.1;
        Granular p2 = (Granular) spawnParticle(9, 5, Color.WHITE, Granular.class);
        p2.velX = -0.1;
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

        ParticleGrid grid = Particle.getGrid();
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(2, 2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        // The state of the game grid is drawn to an image buffer,
        // which is then drawn to the screen

        AffineTransform transform = new AffineTransform();
        transform.scale(scale, scale);
        g2.setTransform(transform);

        grid.draw(g2);

        g2.drawOval(Mouse.X() - cursorSize / 2, Mouse.Y() - cursorSize / 2, cursorSize, cursorSize);
        transform.setToIdentity();

        g2.setTransform(transform);

        Foreground.draw(g2);
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

    /**
     * Draw a cluster of particles on the screen given (x, y) coords and a diameter
     * 
     * @param mx
     * @param my
     * @param diameter
     */
    public void paintParticleCluster(int mx, int my, int diameter) {

        // TODO The only way this method would ever be called is through direct user
        // input
        // so maybe belongs in some sort of UIEvents class

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

    // Copied this from a tutorial and don't know what it does; don't mess with it:
    // apparently a better way of making a game loop timer
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            // updateBufferedImage();
            repaint();

            if (mouseDown)
                paintParticleCluster(Mouse.X(), Mouse.Y(), cursorSize);

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
