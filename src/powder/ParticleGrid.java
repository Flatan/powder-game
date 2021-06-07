package powder;

import java.util.AbstractCollection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Consumer;

import core.Application;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Effectively allows a 2D array of Particles to be used as a cartesian grid.
 */
public class ParticleGrid extends AbstractCollection<Particle> {

  private Particle[][] a;
  private int W;
  private int H;
  private BufferedImage image;
  private Arena arena = new Arena();
  private HashSet<Particle> particles = new HashSet<Particle>();

  public ParticleGrid(int W, int H) {
    a = new Particle[W][H];
    this.W = W;
    this.H = H;
    image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
  }

  public void reset(int W, int H) {
    a = new Particle[W][H];
    this.W = W;
    this.H = H;
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
      p.x = x;
      p.y = y;
      a[x][a.length - 1 - y] = p;
      return true;
    }

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

    int py = p.Y();
    int px = p.X();

    if (set(x, y, p)) {
      a[px][a.length - 1 - py] = null;
      return true;
    }
    return false;
  }

  private class Coord {
    int x;
    int y;

    Coord(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object obj) {

      if (obj instanceof Coord) {
        Coord c = (Coord) obj;
        return (c.x == this.x && c.y == this.y);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return x >>> 2 ^ y;
    }
  }

  private class Arena {

    HashSet<Particle[]> colliders;

    Arena() {
      colliders = new HashSet<Particle[]>();
    }

    void add(Particle[] p) {
      colliders.add(p);
    }

    void process() {

      Random r = new Random();
      for (Particle[] p : colliders) {

        if (r.nextBoolean()) {

          p[0].vel.x = 1;
          p[1].vel.x = -1;
        } else {

          p[0].vel.x = -1;
          p[1].vel.x = 1;
        }

        if (p[0] instanceof Border) {
          p[0].vel.x = 0;
          p[0].vel.y = 0;
          p[1].vel.x = 0;
          p[1].vel.y = 0;
        }
        if (p[1] instanceof Border) {
          p[1].vel.x = 0;
          p[1].vel.y = 0;
        }
      }

      colliders = new HashSet<Particle[]>();
    }
  }

  /**
   * Invokes the update() method for each Particle on the grid
   */
  public void updateParticles() {
    // Iterate through the grid and update every pixel with a Particle

    HashMap<Coord, Particle> register = new HashMap<Coord, Particle>();

    forEachParticle((p) -> {
      // p.vel.y -= 0.1;
      p.nx = (int) (p.X() + p.vel.x);
      p.ny = (int) (p.Y() + p.vel.y);

      register.put(new Coord(p.nx, p.ny), p);

    });

    forEachParticle((p) -> {

      if (test(p.nx, p.ny)) {
        arena.add(new Particle[] { register.get(new Coord(p.nx, p.ny)), p });
      }
    });

    register.clear();
    arena.process();

    forEachParticle((p) -> {

      p.nx = (int) (p.X() + p.vel.x);
      p.ny = (int) (p.Y() + p.vel.y);

      move(p, p.nx, p.ny);

    });

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

    AffineTransform transform = new AffineTransform();
    transform.scale(Application.scale, Application.scale);
    g2.setTransform(transform);

    image.setRGB(0, 0, W, H, new int[W * H], 0, 0);
    try {
      forEachParticle((particle) -> {
        image.setRGB(particle.X(), H - 1 - particle.Y(), particle.displayColor.getRGB());
      });
    } catch (ArrayIndexOutOfBoundsException e) {
    }

    g2.drawImage(image, 0, 0, W, H, null);
    transform.setToIdentity();
    g2.setTransform(transform);
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
