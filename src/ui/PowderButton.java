
package ui;

import java.awt.event.MouseEvent;

import core.Application;

/**
 * PowderButton
 */
public class PowderButton extends Button {

  PowderButton() {
    super(600, 200, 70, 20, "Powder");
    enable();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);
    for (Button btn : EventLoader.particleButtons) {
      btn.disable();
    }

    enable();
    Application.board.invokeCommand("setparticletype powder");
  }
}
