package powder;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Effectively allows a 2D array of Particles to be used as a cartesian grid.
 */
class ParticleGrid extends AbstractCollection<Particle> {

  private final Particle[][] a;
  public final int W;
  public final int H;

  ParticleGrid(Particle[][] array) {
    a = array;
    W = a.length;
    H = a[0].length;
  }

  /**
   * Returns a Particle given its cartesian coordinates on the grid
   * 
   * @param x X coordinate
   * @param y Y coordinate
   * @return Particle
   */
  public Particle get(int x, int y) {
    return a[x][a.length - 1 - y];
  }

  public Particle getReal(double x, double y) {
    return get((int) x, (int) y);
  }

  public boolean testAbs(int x, int y) {
    return get(x, y) != null;
  }

  /**
   * Inserts a Particle on the grid at the provided cartesian coordinates
   * 
   * @param x X coordinate
   * @param y Y coordinate
   * @param p Particle
   */
  public void set(int x, int y, Particle p) {
    a[x][a.length - 1 - y] = p;
  }

  /**
   * Deletes a Particle on the grid at the provided cartesian coordinates
   * 
   * @param x
   * @param y
   * @param p
   */
  public void delete(int x, int y, Particle p) {
    a[p.X()][a.length - 1 - p.Y()] = null;
  }

  /**
   * Moves a Particle from its current location to provided cartesian coordinates.
   * 
   * @param x
   * @param y
   * @param p
   */
  public void move(Particle p, int x, int y) {
    delete(x, y, p);
    set(x, y, p);
  }

  /**
   * Invokes the update() method for each Particle on the grid
   */
  public void updateParticles() {
    // Iterate through the grid and update every pixel with a Particle
    forEachParticle((x) -> {
      x.update();
      x.updated = false;
    });
  }

  /**
   * Helper method for mapping a function to every particle on the grid
   * 
   * @param action A lambda expression
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
   * Checks if arbitrary (x,y) coordinates are invalid
   * 
   * @param x X coordinate
   * @param y Y coordinate
   * @return boolean true if invalid else false
   */
  public boolean outOfBounds(double x, double y) {
    return outOfBoundsX(x) || outOfBoundsY(y);
  }

  /**
   * Checks if arbitrary y coordinate is invalid
   * 
   * @param y
   * @return boolean true if invalid else false
   */
  public boolean outOfBoundsY(double y) {
    return (y >= H || y < 0);
  }

  /**
   * Checks if arbitrary x coordinate is invalid
   * 
   * @param x
   * @return boolean true if invalid else false
   */
  public boolean outOfBoundsX(double x) {
    return (x >= W || x < 0);
  }

  /**
   * Required to extend {@link AbstractCollection}
   */
  @Override
  public int size() {
    return (a.length * a[0].length);
  }

  /**
   * Required to extend {@link AbstractCollection}
   */
  @Override
  public Iterator<Particle> iterator() {
    return null;
  }

}
