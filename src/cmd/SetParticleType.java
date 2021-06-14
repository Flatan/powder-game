package cmd;

import core.Command;
import ui.ParticleFactory;

/**
 * SetParticleType
 */
public class SetParticleType implements Command {

  @Override
  public void call(String... args) {

    System.out.println("here");
    System.out.println(args[1]);

    switch (args[1]) {
      case "powder":
        ParticleFactory.element = powder.Granular.class;
        break;

      case "solid":
        ParticleFactory.element = powder.Solid.class;
        break;

      default:
        break;
    }

  }
}
