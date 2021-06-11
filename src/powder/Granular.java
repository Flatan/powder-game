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
   * @param p1	particle 1
   * @param p2	particle 2
   * @param cR    Coefficient of Restitution. Should be between 0-1. Or SLIGHTLY
   *              higher for fun bouncy effect
   * @return the exerted impulse
   */
  public static double collide(Particle p1, Particle p2, double cR) {
	  double impulse;
	  impulse = (p1.mass*p2.mass)/(p1.mass+p2.mass);
	  impulse *= p2.vel.magnitude() - p1.vel.magnitude();
	  impulse *= 1 + cR;
	  return impulse;

  }
}
