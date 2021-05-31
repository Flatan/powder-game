package ui;

import java.awt.Graphics2D;
import java.awt.Color;
import powder.Application;
import powder.Board;

public class Foreground {

      public static void draw(Graphics2D g2) {

            Board board = Application.board;

            g2.drawString("Hotkeys:", 0, 20);
            g2.drawString("p - powder", 0, 40);
            g2.drawString("s - solid", 0, 60);
            g2.drawString("t - toggle heat map display", 0, 80);
            g2.drawString("c - cold particles", 0, 100);
            g2.drawString("w - warm particles", 0, 120);
            g2.drawString("h - hot particles", 0, 140);
            g2.drawString("1 - low resolution", 0, 160);
            g2.drawString("2 - high resolution", 0, 180);

            if (board.getFPS() < 40)
                  g2.setColor(Color.red);
            else
                  g2.setColor(Color.white);
            g2.drawString(String.format("FPS: %.2f", board.getFPS()), board.getWidth() * (int) board.getScale() - 100,
                        20);
      }

}