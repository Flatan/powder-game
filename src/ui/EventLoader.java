
package ui;

import core.Board;
import java.util.ArrayList;
import cmd.*;

/**
 * EventLoader
 */
public final class EventLoader {

  static ParticleFactory p = new ParticleFactory();

  static ArrayList<Button> particleButtons = new ArrayList<>();

  public static TextFX fx = new TextFX(0, 0, "");

  public static void loadEvents(Board B) {

    Button pb = new PowderButton();
    Button sb = new SolidButton();
    Label cmd = new CommandLine();

    particleButtons.add(pb);
    particleButtons.add(sb);

    B.add(pb);
    B.add(sb);
    B.add(cmd);

    B.add(fx);
    B.add(p);
    B.addMouseListener(p);

  }
}
