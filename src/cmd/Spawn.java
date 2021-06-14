
package cmd;

import core.Command;
import math.Vector2D;

import java.util.Random;

import core.*;
import powder.*;

/**
 * Spawn
 */
public class Spawn implements Command {

  @Override
  public void call(String... args) {

    if (args[1].equals("pop")) {

      Random r = new Random();
      for (int i = 0; i < 50; i++) {
        Application.grid.spawn(r.nextInt(50) + 275, r.nextInt(50) + 275, Granular.class).vel = new Vector2D(
            r.nextInt(20) - 10, r.nextInt(20) - 10);
      }

      return;
    }

    int x = Integer.parseInt(args[1]);
    int y = Integer.parseInt(args[2]);
    int velx = 0;
    int vely = 1;

    if (args.length == 5) {

      velx = Integer.parseInt(args[3]);
      vely = Integer.parseInt(args[4]);

    }
    Particle p = Application.grid.spawn(x, y, Granular.class);
    p.temperature = 50;
    p.vel.x = velx;
    p.vel.y = vely;
  }

}
