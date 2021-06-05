package powder;

import java.util.AbstractCollection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Consumer;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.lang.reflect.Constructor;

/**
 * Effectively allows a 2D array of Particles to be used as a cartesian grid.
 */
public class ParticleGrid extends AbstractCollection<Particle> {

  private final Particle[][] a;
  public final int W;
  public final int H;
  private BufferedImage image;
  private HashSet<Particle> particles = new HashSet<Particle>();

  public ParticleGrid(Particle[][] array) {
    a = array;
    W = a.length;
    H = a[0].length;
    image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Returns a Particle given its cartesian coordinates on the grid
   * 
   * @param x X coordinate
   * @param y Y coordinate
   * @return Particle
   */

  private Particle _get(int x, int y) {

    if (outOfBounds(x, y)) {
      return null;
    }

    return a[x][a.length - 1 - y];
  }

  public Particle get(int x, int y) {

    return _get(x, y);
  }

  public Particle get(double x, double y) {

    return _get((int) x, (int) y);
  }

  public boolean test(double x, double y) {
    return get(x, y) != null;
  }

  public boolean test(int x, int y) {
    return get(x, y) != null;
  }

  /**
   * Inserts a Particle on the grid at the provided cartesian coordinates
   * 
   * @param x X coordinate
   * @param y Y coordinate
   * @param p Particle
   * @return whether set was successful
   */

  public boolean set(int x, int y, Particle p) {
    if (test(x, y) || outOfBounds(x, y))
      return false;
    else {
      particles.add(p);
      a[x][a.length - 1 - y] = p;
      return true;
    }

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
   * @return whether move was successful
   */
  public boolean move(Particle p, int x, int y) {
    if (test(x, y) || outOfBounds(x, y)) {
      return false;
    }
    delete(x, y, p);
    set(x, y, p);
    return true;
  }

  /**
   * Invokes the update() method for each Particle on the grid
   */
  public void updateParticles() {
    // Iterate through the grid and update every pixel with a Particle

    forEachParticle(x -> x.update());

    forEachParticle(x -> x.updated = false);
  }

  public boolean computeIfPresent(double x, double y, Consumer<Particle> action) {

    Particle p = get(x, y);
    if (p != null) {
      action.accept(p);
      return true;
    }
    return false;

  }

  public boolean computeIfPresent(int x, int y, Consumer<Particle> action) {

    Particle p = get(x, y);
    if (p != null) {
      action.accept(p);
      return true;
    }
    return false;

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
   * The single frame drawing entry point used by Board. Rasterizes the 2D array
   * of Particles into a BufferedImage
   * 
   * @param g2 Graphics2D
   */
  public void draw(Graphics2D g2) {

    image.setRGB(0, 0, W, H, new int[W * H], 0, 0);
    try {
      forEachParticle((particle) -> {
        image.setRGB(particle.X(), H - 1 - particle.Y(), particle.displayColor.getRGB());
      });
    } catch (ArrayIndexOutOfBoundsException e) {
    }

    g2.drawImage(image, 0, 0, W, H, null);
  }

  /**
   * Initialize a new particle on the grid of a specific type
   * 
   * @param x           X coordinate
   * @param y           Y coordinate
   * @param elementType Class which extends Particle
   * 
   */
  public Particle spawn(int x, int y, Color color, Class<? extends Particle> elementType) {

    Particle particle = null;
    try {
      Constructor<?> cons = elementType.getDeclaredConstructor(int.class, int.class, Color.class);
      particle = (Particle) cons.newInstance(x, y, color);
    } catch (Throwable e) {
      System.out.println(e);
    }
    set(x, y, particle);
    return particle;
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
