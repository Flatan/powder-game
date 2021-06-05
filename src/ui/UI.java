package ui;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.ArrayDeque;

import core.Board;

/**
 * UI
 *
 * Class that acts like a "UI core" that dispatches events and also has a direct
 * line of communication with the main application core. Holds instances of
 * keyboard and mouse for easy access.
 */
public class UI {

      private Board B;

      HashSet<Integer> positionBuffer = new HashSet<Integer>();
      Graphics2D g2;

      static final Keyboard keyboard = new Keyboard();
      static final Mouse mouse = new Mouse();

      public UI(Board B) {
            this.B = B;
            B.addKeyListener(UI.keyboard);
            B.addMouseWheelListener(UI.mouse.wheelControls);
            B.addMouseListener(UI.mouse.adapter);
            B.connectEvent(PaintParticleCluster.class);
            B.connectEvent(ShowHeatMap.class);
            B.connectEvent(AlwaysOn.class);
            B.connectEvent(Spinner.class);
            B.connectEvent(Resolution.class);
      }

      /**
       * Simple tool for storing lines of text and drawing them
       */
      public class TextBuffer {

            private ArrayDeque<String> q;
            int vertSpacing;
            int x = 0;
            int y = 0;
            int padX;
            int padY;
            int fs;
            Graphics2D g;

            TextBuffer(Graphics2D g, int vertSpacing, int padX, int padY) {
                  this.vertSpacing = vertSpacing;
                  this.q = new ArrayDeque<String>();
                  this.g = g;
                  this.padX = padX;
                  this.padY = padY;
                  this.fs = g.getFont().getSize();
            }

            /**
             * Simplifies formatting a key value pair where the value is a double
             * 
             * @param key
             * @param value
             */
            public void addPair(String key, double value) {
                  q.add(String.format((key + "%.2f"), value));
            }

            /**
             * Simplifies formatting a key value pair where the value is an int
             * 
             * @param key
             * @param value
             */
            public void addPair(String key, int value) {
                  q.add(String.format((key + "%d"), value));
            }

            /**
             * Adds a single String to the internal queue
             * 
             * @param str
             */
            public void add(String str) {
                  q.add(str);
            }

            /**
             * Internal flush(). Empties the queue into g.drawString()
             */
            private void _flush() {
                  while (!q.isEmpty()) {
                        y += fs + vertSpacing;
                        g.drawString(q.remove(), x + padX, y + padY);
                  }
            }

            /**
             * Draw the contents of the buffer to the interally stored Graphics2D object.
             * When no x,y position is given, continue from the previous position
             */
            public void flush() {
                  _flush();
            }

            /**
             * Draw the contents of the buffer to the internally stored Graphics2D object.
             * Draws from the upper left corner of the provided coordinates.
             * 
             * @param x
             * @param y
             */
            public void flush(int x, int y) {
                  this.x = x;
                  this.y = y;
                  _flush();
            }
      }

      /**
       * Iterates through every the .draw() method of every connected {@link UIEvent}.
       * It provides .draw() with the active Graphics2D object and a helper class for
       * drawing text {@link TextBuffer} (which also wraps the same Graphics2D). This
       * is the main dispatcher of the "UI core".
       * 
       * @param g2
       */
      public void draw(Graphics2D g2) {

            this.g2 = g2;

            TextBuffer t = new TextBuffer(g2, 5, 5, 0);

            for (UIEvent event : B.getConnectedEvents()) {
                  event.draw(t, g2);
            }

      }

}
