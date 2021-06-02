package ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import powder.Board;
import powder.Granular;
import powder.Solid;
import powder.Application;

public class KeyAction implements KeyListener {

    private Board B;
    private char lastKeyPressed;
    private char prevKeyPressed = Character.MIN_VALUE;
    private int sameKeyCount = 1;
    private HashSet<Character> keyBuffer = new HashSet<Character>();

    public char lastKeyPressed() {
        return lastKeyPressed;
    }

    public char prevKeyPressed() {
        return prevKeyPressed;
    }

    public int keyRepeated(char c) {

        if (lastKeyPressed == c) {
            return sameKeyCount;
        }
        return 0;
    }

    public boolean keyToggled(char c) {

        if (keyBuffer.contains(c)) {
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
        char keyChar = e.getKeyChar();

        if (keyBuffer.contains(keyChar)) {
            keyBuffer.remove(keyChar);
        } else {
            keyBuffer.add(keyChar);
        }

        prevKeyPressed = lastKeyPressed;
        lastKeyPressed = e.getKeyChar();

        if (lastKeyPressed == prevKeyPressed) {
            sameKeyCount++;
        } else {
            sameKeyCount = 1;
        }

        switch (e.getKeyChar()) {
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
