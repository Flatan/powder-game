
package ui;

import java.awt.event.MouseEvent;
import core.Application;

/**
 * PowderButton
 */
public class SolidButton extends Button {

  SolidButton() {
    super(680, 200, 70, 20, "Solid");
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);
    for (Button btn : EventLoader.particleButtons) {
      btn.disable();
    }
    enable();
    Application.board.invokeCommand("setparticletype solid");
  }
}
