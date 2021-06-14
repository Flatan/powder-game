
package cmd;

import core.Command;
import core.*;
import powder.*;

/**
 * Spawn
 */
public class Spawn implements Command {

  @Override
  public void call(String... args) {

    if (args[1].equals("star")) {

      for (int i = 0; i < 100; i++) {

        Particle p = Application.grid.spawn(10, 10 + i, Granular.class);
        p.temperature = 50;
        p.vel.x = 2;
        p.vel.y = 0;
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
