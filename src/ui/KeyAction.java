package ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Hashtable;

import powder.Board;
import powder.Granular;
import powder.Solid;
import powder.Application;

public class KeyAction implements KeyListener {

    private Board B;
    private char c = Character.MIN_VALUE;
    private HashSet<Character> toggleBuffer = new HashSet<Character>();
    private Hashtable<Character, Integer> countBuffer = new Hashtable<Character, Integer>();

    /**
     * Returns the last key pressed by the user. Will return Character.MIN_VALUE as
     * a placeholder for "null".
     * 
     * @return char
     */
    public char lastKeyPressed() {
        return c;
    }

    /**
     * Returns the number of times a key has been repeated in a row
     * 
     * @param c the character to check
     * @return int repeat count
     */

    public int keyRepeated(char c) {
        return countBuffer.getOrDefault(c, 0);
    }

    /**
     * Tests if a given key is currently toggled
     * 
     * @param c the character to test against
     * @return boolean true if toggled false if not
     */
    public boolean keyToggled(char c) {

        if (toggleBuffer.contains(c)) {
            return true;
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Dispatch key presses based on state
    @Override
    public void keyPressed(KeyEvent e) {

        B = Application.getBoard();
        char c = e.getKeyChar();

        if (countBuffer.containsKey(c)) {
            countBuffer.replace(c, countBuffer.get(c) + 1);
        } else {
            countBuffer.clear();
            countBuffer.put(c, 1);
        }

        if (toggleBuffer.contains(c)) {
            toggleBuffer.remove(c);
        } else {
            toggleBuffer.add(c);
        }

        switch (c) {
            case 'p':
                B.setSelectedElement(Granular.class);
                B.setSelectedColor(Color.white);
                break;
            case 's':
                B.setSelectedElement(Solid.class);
                B.setSelectedColor(Color.gray);
                break;
            case 't':
                // UIEvent.toggleHeatMap();
                break;
            case 'c':
                B.setSelectedTemp(0);
                break;
            case 'w':
                B.setSelectedTemp(50);
                break;
            case 'h':
                B.setSelectedTemp(100);
                break;
            case '0':
                B.setScale(60);
                B.setWidth(10);
                B.setHeight(10);
                B.setDelay(100);
                B.reset();
                break;
            case '1':
                // Particle.setGravity(-0.5);
                B.setScale(2);
                B.setWidth(300);
                B.setHeight(300);
                B.setDelay(25);
                B.reset();
                break;
            case '2':
                // Particle.setGravity(-0.5);
                B.setScale(1);
                B.setWidth(600);
                B.setHeight(600);
                B.setDelay(25);
                B.reset();
                break;
            case ' ':
                B.testCollison();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
