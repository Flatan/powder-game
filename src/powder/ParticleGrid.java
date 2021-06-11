package powder;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.ArrayDeque;

import core.Application;
import ui.ParticleFactory;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Constructor;

/**
 * Effectively allows a 2D array of Particles to be used as a cartesian grid.
 */
public class ParticleGrid {

  private Particle[][] a;
  private int W;
  private int H;
  private BufferedImage image;
  private Arena arena = new Arena();
  private ArrayDeque<Particle> spawnQueue = new ArrayDeque<Particle>();
  private AffineTransform transform = new AffineTransform();

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
    spawnQueue = new ArrayDeque<Particle>();

    ParticleFactory.element = Solid.class;
    ParticleFactory.spawnRect(0, 0, W, H, 3);
    ParticleFactory.element = Granular.class;

    image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Returns a Particle given its cartesian coordinates on the grid
   * 
   * @param x X coordinate
   * @param y Y coordinate
   * @return Particle
   */
  public Particle get(int x, int y) {
    if (outOfBounds(x, y)) {
      return null;
    }
    return a[x][a.length - 1 - y];
  }

  public boolean test(int x, int y) {
    return get(x, y) != null;
  }

  /**
   * Forcefully inserts particle onto grid
   * 
   * @param p
   * @return boolean successful or not
   */
  private boolean set(Particle p) {

    if (outOfBounds(p.x, p.y)) {
      System.out.printf("\n Tried to set particle to (%d,%d)", p.x, p.y);
      System.out.printf("\n (Interally, grid.a[%d][%d - 1 - %d])", p.x, a.length, p.y);
      System.out.printf("\n (           grid.a[p.x][a.length - 1 - p.y])");
      System.out.printf("\n Particle type: %s", p.getClass().toString());
      System.out.printf("\n Location: (%d, %d)", p.x, p.y);
      System.out.printf("\n Velocity: velx:%.2f vely:%.2f", p.vel.x, p.vel.y);
      return false;
    }
    a[p.x][a.length - 1 - p.y] = p;
    return true;
  }

  /**
   * Moves a Particle from its current location to provided cartesian coordinates.
   * 
   * @param x
   * @param y
   * @param p
   * @return whether move was successful
   */
  public void move(Particle p, int x, int y) {

    int py = p.y;
    int px = p.x;

    p.y = y;
    p.x = x;

    spawnQueue.add(p);

    a[px][a.length - 1 - py] = null;
  }

  private class Arena {

    Random r = new Random();
    HashSet<Particle[]> colliders;

    Arena() {
      colliders = new HashSet<Particle[]>();
    }

    void add(Particle[] p) {
      colliders.add(p);
    }

    void process() {

      for (Particle[] p : colliders) {

        // TODO Vector equations that produce new vectors for each particle.

        // p[0] <---> p[1]
        p[0].vel = p[1].giveVel;
        p[1].vel = p[0].giveVel;

      }

      colliders = new HashSet<Particle[]>();
    }
  }

  /**
   * Runs the vector calculations for each Particle on the grid
   */
  public void updateParticles() {
    // Iterate through the grid and update every pixel with a Particle
    while (!spawnQueue.isEmpty()) {

      Particle p = spawnQueue.removeLast();
      a[p.ox][a.length - 1 - p.oy] = null;
      set(p);
    }

    forEachParticle((p) -> {

      int nx = (int) (p.x + p.vel.x);
      int ny = (int) (p.y + p.vel.y);

      if (test(nx, ny) && get(nx, ny) != p) {
        arena.add(new Particle[] { get(nx, ny), p });
      }
    });

    arena.process();

    forEachParticle((p) -> {

      p.ox = p.x;
      p.oy = p.y;
      p.y = (int) (p.y + p.vel.y);
      p.x = (int) (p.x + p.vel.x);

      p.updateProperties();
      spawnQueue.add(p);

    });
  }

  /**
   * Helper method for mapping a function to every particle on the grid
   * 
   * @param action A lambda expression
   */
  private void forEachParticle(Consumer<Particle> action) {

    for (int x = 0; x < W; x++) {
      for (int y = 0; y < H; y++) {
        if (test(x, y))
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

    transform.scale(Application.scale, Application.scale);
    g2.setTransform(transform);
    image.setRGB(0, 0, W, H, new int[W * H], 0, 0);

    forEachParticle((particle) -> {
      image.setRGB(particle.x, H - 1 - particle.y, particle.displayColor.getRGB());
    });

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
  public Particle spawn(int x, int y, Class<? extends Particle> elementType) {

    Particle particle = null;

    try {
      Constructor<?> cons = elementType.getDeclaredConstructor(int.class, int.class);
      particle = (Particle) cons.newInstance(x, y);
    } catch (Throwable e) {
      System.out.println(e);
    }

    spawnQueue.add(particle);
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
    return (y >= H || y < 0 || x >= W || x < 0);
  }

}
