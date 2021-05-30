package powder;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

class ParticleGrid extends AbstractCollection<Particle> {

  private final Particle[][] a;
  public final int W;
  public final int H;

  ParticleGrid(Particle[][] array) {

    a = array;
    W = a.length;
    H = a[0].length;

  }

  public Particle get(int x, int y) {

    try {

      return a[x][a.length - 1 - y];

    } catch (Exception e) {

      return null;
    }

  }

  public void set(int x, int y, Particle element) {

    a[x][a.length - 1 - y] = element;

  }
  
  public void move(Particle element, int x, int y) {
	  a[element.getGridX()][a.length - 1 - element.getGridY()] = null;
	  a[x][a.length - 1 - y] = element;
  }

  /**
   * Helper method for mapping a function to every particle on the grid
   * 
   * @param action The lambda expression to use
   */
  public void forEachParticle(Consumer<Particle> action) {
    for (int x = 0; x < W; x++) {
      for (int y = 0; y < H; y++) {
        if (get(x, y) != null)
          action.accept(get(x, y));
      }
    }
  }

  /**
   * Checks if arbitrary (x,y) coordinates fall within the bounds of the particle
   * grid
   * 
   * @param x X coordinate
   * @param y Y coordinate
   * @return boolean true if coordinates fall outside else false
   */
  public boolean outOfBounds(int x, int y) {

    return (x >= W || y >= H || x < 0 || y < 0);
  }

  @Override
  public int size() {
    return (a.length * a[0].length);
  }

  @Override
  public Iterator<Particle> iterator() {
    return null;
  }

}
