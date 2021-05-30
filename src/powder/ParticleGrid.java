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
