package ui;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.HashSet;
import java.util.ArrayDeque;

import core.Board;

public class UI {

      private Mouse M;
      private Board B;

      HashSet<Integer> positionBuffer = new HashSet<Integer>();
      Graphics2D g2;

      public UI(Board B) {
            this.B = B;
            this.M = B.getMouse();
            B.connectEvent(PaintParticleCluster.class);
            B.connectEvent(ShowHeatMap.class);
            B.connectEvent(GlobalSettings.class);
            B.connectEvent(Spinner.class);
      }

      public class DrawQueue {

            private ArrayDeque<String> q;
            int vertSpacing;
            int x = 0;
            int y = 0;
            int fs;
            Graphics2D g;

            DrawQueue(Graphics2D g, int vertSpacing) {
                  this.vertSpacing = vertSpacing;
                  this.q = new ArrayDeque<String>();
                  this.g = g;

                  this.fs = g.getFont().getSize();
            }

            public void addPair(String key, double value) {
                  q.add(String.format((key + "%.2f"), value));
            }

            public void addPair(String key, int value) {
                  q.add(String.format((key + "%d"), value));
            }

            public void add(String str) {
                  q.add(str);
            }

            public void flush() {
                  while (!q.isEmpty()) {
                        g.drawString(q.remove(), x, y);
                        y += fs + vertSpacing;
                  }
            }

            public void flush(int x, int y) {
                  y += fs + vertSpacing;
                  while (!q.isEmpty()) {
                        g.drawString(q.remove(), x, y);
                        y += fs + vertSpacing;
                  }

                  this.x = x;
                  this.y = y;
            }
      }

      public void draw(Graphics2D g2) {

            int cursorSize = M.getCursorSize();
            this.g2 = g2;

            g2.drawOval(Mouse.windowX() - cursorSize / 2, Mouse.windowY() - cursorSize / 2, cursorSize, cursorSize);

            DrawQueue Q = new DrawQueue(g2, 5);
            Q.add("q - quit");
            Q.add("p - powder");
            Q.add("s - solid");
            Q.add("1 - low resolution");
            Q.add("2 - high resolution");
            Q.flush(5, 0);

            if (B.getFPS() < 40)
                  g2.setColor(Color.red);
            else
                  g2.setColor(Color.white);

            Q.addPair("FPS: ", B.getFPS());
            Q.flush(B.getWidth() - 200, 0);
            g2.setColor(Color.white);

            Q.addPair("Spawn count: ", Board.runtimeParticleCount);

            for (UIEvent event : B.getConnectedEvents()) {
                  event.draw(Q);
            }

      }

}
