
package ui;

import core.Board;

/**
 * EventLoader
 */
public final class EventLoader {

  public static void loadEvents(Board B) {

    B.connectEvent(AlwaysOn.class);
    B.connectEvent(ParticleFactory.class);
    B.connectEvent(ShowHeatMap.class);
    B.connectEvent(Spinner.class);
    B.connectEvent(Resolution.class);
    B.connectEvent(Button.class);
    B.connectEvent(Logger.class);

  }
}
