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

public class Board extends JPanel
        implements Runnable {

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    
    //Milliseconds per frame:
    private final int DELAY = 25;

    private Thread animator;
	private int mouseX;
	private int mouseY;
	private int cursorSize = 20;
	
	private boolean mouseDown = false;
	private BufferedImage image;
    
   
    public Board() {

        initBoard();
    }


    private void initBoard() {

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        image = new BufferedImage(B_WIDTH, B_HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
        		mouseDown = true;
        	}
        	public void mouseReleased(MouseEvent e) {
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(Color.WHITE);
        
        g2.drawImage(image, 0, 0, null);
        g2.drawOval(mouseX-cursorSize/2, mouseY-cursorSize/2, cursorSize, cursorSize);
    }


    private void cycle() {
    	try {mouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;}
        catch (IllegalComponentStateException e2) {}
        
    	try {mouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;}
     	catch (IllegalComponentStateException e2) {}
    	
    	Particle[][] grid = Particle.getGrid();
    	
		for (int x = mouseX-cursorSize/2; x < mouseX+cursorSize/2; x++) {
		for (int y = mouseY-cursorSize/2; y < mouseY+cursorSize/2; y++) {
			if (x<B_WIDTH && x>0)
			if (y<B_HEIGHT && y>0)
	    	if (mouseDown && grid[x][y] == null) 
	    	if (Math.hypot(x-mouseX, y-mouseY)<=cursorSize/2)
	    	{
	    		new Granular(x,y,Color.WHITE);
	    	}
			
		}}
    	
    		
    	
		Particle.updateGrid();

		
    	for ( int x = 0; x < B_WIDTH; x++ ) {
    	for ( int y = 0; y < B_HEIGHT; y++ ) {
			if (grid[x][y] != null)
				image.setRGB(x, y, grid[x][y].color.getRGB() );
			else
				image.setRGB(x, y, Color.BLACK.getRGB() );
    	}}
    }

    
    
    //Copied this from a tutorial and don't know what it does; don't mess with it:
    //apparently a better way of making a game loop timer
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
                
                JOptionPane.showMessageDialog(this, msg, "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}