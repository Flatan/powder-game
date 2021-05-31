package ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import powder.Board;
import powder.Granular;
import powder.Solid;
import powder.Application;
import powder.Particle;

public class KeyAction implements KeyListener {

    private Board board;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Dispatch key presses based on state
    @Override
    public void keyPressed(KeyEvent e) {

        board = Application.board;

        switch (e.getKeyChar()) {
        case 'p':
            board.setSelectedElement(Granular.class);
            board.setSelectedColor(Color.white);
            break;
        case 's':
            board.setSelectedElement(Solid.class);
            board.setSelectedColor(Color.gray);
            break;
        case 't':
            Particle.toggleHeatMap();
            break;
        case 'c':
            board.setSelectedTemp(0);
            break;
        case 'w':
            board.setSelectedTemp(50);
            break;
        case 'h':
            board.setSelectedTemp(100);
            break;
        case '0':
            board.setScale(60);
            board.setWidth(10);
            board.setHeight(10);
            board.setDelay(100);
            board.reset();
            break;
        case '1':
            Particle.setGravity(-0.5);
            board.setScale(2);
            board.setWidth(300);
            board.setHeight(300);
            board.setDelay(25);
            board.reset();
            break;
        case '2':
            Particle.setGravity(-0.5);
            board.setScale(1);
            board.setWidth(600);
            board.setHeight(300);
            board.setDelay(25);
            board.reset();
            break;
        case ' ':
            board.testCollison();
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
