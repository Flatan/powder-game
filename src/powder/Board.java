package powder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import color.ColorGradientMap;
import ui.KeyAction;
import ui.Mouse;
import ui.PaintParticleCluster;
import ui.ShowHeatMap;
import ui.UIEvent;
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

    // Defines the area of powder placement
    public int cursorSize = 20;

    private KeyAction ka = new KeyAction();
    private final Mouse M = new Mouse();
    // private UIEvent E = new UIEvent();
    // private BufferedImage image;
    private Thread animator;
    private Particle p = null;

    private Class<? extends Particle> selectedElement = Granular.class;
    private ArrayList<UIEvent> UIevents = new ArrayList<UIEvent>();

    private Color selectedColor = Color.white;
    private double selectedTemp = 50;
    private Foreground fg = new Foreground();

    public Board() {

        initBoard();

    }

    // Setup initial settings and event listeners
    private void initBoard() {
        reset();

        connectEvent(PaintParticleCluster.class);
        connectEvent(ShowHeatMap.class);

        Particle.heatmap.addColor(0, Color.GREEN);
        Particle.heatmap.addColor(50, Color.YELLOW);
        Particle.heatmap.addColor(100, Color.RED);
        setFocusable(true);
        addKeyListener(ka);
        addMouseWheelListener(M.wheelControls);
        addMouseListener(M.adapter);
    }

    /**
     * Set the current element
     * 
     * @param element
     */
    public void setSelectedElement(Class<? extends Particle> element) {
        this.selectedElement = element;
    }

    public Class<? extends Particle> getSelectedElement() {
        return selectedElement;
    }

    /**
     * Get the board's mouse object
     * 
     * @return
     */
    public Mouse getMouse() {
        return M;
    }

    public KeyAction getKeyboard() {
        return ka;
    }
    // public UIEvent getUIEvents() {
    // return E;
    // }

    /**
     * Set the color of the current element
     * 
     * @param c
     */
    public void setSelectedColor(Color c) {
        this.selectedColor = c;
    }

    /**
     * Get the color of the current element
     *
     * @return Color
     */
    public Color getSelectedColor() {
        return selectedColor;
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
     * Get the selected temp
     *
     * @return double
     */
    public double getSelectedTemp() {
        return selectedTemp;
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

    private void connectEvent(Class<? extends UIEvent> event) {

        try {
            UIEvent instance = event.getDeclaredConstructor().newInstance();
            UIevents.add(instance);
        } catch (Exception e) {
            // TODO: handle exception
        }
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
        ParticleGrid grid = Particle.getGrid();
        Particle.gravity = 0;
        Granular p1 = (Granular) grid.spawnParticle(0, 5, Color.WHITE, Granular.class);
        p1.velX = 0.1;
        Granular p2 = (Granular) grid.spawnParticle(9, 5, Color.WHITE, Granular.class);
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

        transform.setToIdentity();

        g2.setTransform(transform);

        fg.draw(g2);
    }

    // Copied this from a tutorial and don't know what it does; don't mess with it:
    // apparently a better way of making a game loop timer
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            for (int i = 0; i < UIevents.size(); i++) {
                UIEvent instance = UIevents.get(i);
                if (instance.sendingSignal()) {
                    instance.eventOn();
                } else {
                    instance.eventOff();
                }
            }

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
