package powder;

import java.awt.Color;
import java.util.function.*;

import math.Vector2D;

/**
 * Granular
 *
 * Extension of the Particle class that is affected by gravity
 *
 */

public class Granular extends Particle {

  Granular(int x, int y) {
    super(x, y, Color.white);
    vel.y = -1;
    giveVel = new Vector2D(0.5, 0.5);
  }

  /**
   * Calculates collision between two particles
   * 
   * @param other The other particle
   * @param cR    Coefficient of Restitution. Should be between 0-1. Or SLIGHTLY
   *              higher for fun bouncy effect
   */
  public void collide(Particle other, double cR) {

    if (other.dynamic) {
      BiFunction<Double, Double, Double> newVel = (a, b) -> (cR * (b - a) + a + b) / 2;

      double tempVelX = vel.x;
      double tempVelY = vel.y;

      vel.x = newVel.apply(vel.x, other.vel.x);
      vel.y = newVel.apply(vel.y, other.vel.y);

      other.vel.x = newVel.apply(other.vel.x, tempVelX);
      other.vel.y = newVel.apply(other.vel.y, tempVelY);
    } else
      vel = new Vector2D(0, 0);

  }
}
