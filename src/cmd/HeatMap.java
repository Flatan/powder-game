package cmd;

import core.Command;
import powder.Particle;

/**
 * HeatMap
 */
public class HeatMap implements Command {

  @Override
  public void call(String... args) {

    System.out.println("called heatmap");
    Particle.showHeatMap = true;
  }

}
