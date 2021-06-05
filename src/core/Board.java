package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.HashSet;

import ui.Keyboard;
import ui.Mouse;
import ui.UIEvent;
import ui.UI;
import powder.*;

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

    private Keyboard ka = new Keyboard();
    private final Mouse M = new Mouse();
    // private UIEvent E = new UIEvent();
    // private BufferedImage image;
    private Thread animator;
    private Particle p = null;

    private Class<? extends Particle> selectedElement = Granular.class;
    private ArrayList<UIEvent> UIevents = new ArrayList<UIEvent>();

    private Color selectedColor = Color.white;
    private double selectedTemp = 50;
    private UI ui;

    public Board() {

        initBoard();

    }

    // Setup initial settings and event listeners
    private void initBoard() {
        reset();

        ui = new UI(this);

        Particle.heatmap.addColor(0, Color.GREEN);
        Particle.heatmap.addColor(50, Color.YELLOW);
        Particle.heatmap.addColor(100, Color.RED);

        Particle.slopemap.addColor(-5, Color.RED);
        Particle.slopemap.addColor(0, Color.WHITE);
        Particle.slopemap.addColor(5, Color.GREEN);

        setFocusable(true);
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

    public Keyboard getKeyboard() {
        return ka;
    }

    /**
     * Get the initialized UI events
     * 
     * @return
     */
    public ArrayList<UIEvent> getConnectedEvents() {
        return UIevents;
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

    public void connectEvent(Class<? extends UIEvent> event) {

        try {
            UIEvent instance = event.getDeclaredConstructor().newInstance();
            UIevents.add(instance);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void reset() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension((int) (B_WIDTH * scale), (int) (B_HEIGHT * scale)));
        ParticleGrid grid = new ParticleGrid(new Particle[B_WIDTH][B_HEIGHT]);
        Particle.grid = grid;

        /*
         * for (int x = 1; x<B_WIDTH; x++) {
         * grid.spawnParticle(x,x,Color.RED,Solid.class); }
         */

        // image = new BufferedImage(B_WIDTH, B_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // bgGrid = new int[B_WIDTH * B_HEIGHT];
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

        ui.draw(g2);
    }

    // Copied this from a tutorial and don't know what it does; don't mess with it:
    // apparently a better way of making a game loop timer
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;
        char prevInstance = Character.MIN_VALUE;

        HashSet<UIEvent> ActiveEventBuffer = new HashSet<UIEvent>();

        beforeTime = System.currentTimeMillis();

        while (true) {

            // Allows the KeyPressed() event to deliver the key only once
            // by immediately switching instanceBuffer back to "MIN_VALUE"
            // (char placeholder for "null")
            if (ka.instanceBuffer == prevInstance) {
                ka.instanceBuffer = Character.MIN_VALUE;
            } else {
                prevInstance = ka.instanceBuffer;
            }

            // Stream each UIEvent if its "sendingSignal()" gate is open
            for (UIEvent E : UIevents) {
                if (E.trigger()) {

                    if (!ActiveEventBuffer.contains(E)) {
                        E.on(true);
                        ActiveEventBuffer.add(E);
                    } else {
                        E.on(false);
                    }
                } else {

                    if (ActiveEventBuffer.contains(E)) {
                        E.off(true);
                        ActiveEventBuffer.remove(E);
                    } else {
                        E.off(false);
                    }
                }
            }

            // The particles can't be updated from within the paintComponent method because
            // for some dumb reason repaint() doesn't wait to finish running before the
            // program continues on
            // and it breaks the framerate counter so I had to move updateParticles() out
            // here

            // Also potentially means the drawing of buffered image could be causing lag not
            // registered by
            // the counter but idk
            Particle.getGrid().updateParticles();
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
