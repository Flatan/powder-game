package powder;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.ArrayDeque;

import core.Application;
import math.Point;
import math.Vector2D;
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
  private Point target = new Point(0, 0);

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
  public Particle get(Point p) {
    if (outOfBounds(p)) {
      return null;
    }
    return a[(int) p.x][a.length - 1 - (int) p.y];
  }

  public boolean test(Point p) {
    return get(p) != null;
  }

  /**
   * Forcefully inserts particle onto grid
   * 
   * @param p
   * @return boolean successful or not
   */
  private boolean set(Particle p) {

    if (outOfBounds(p.loc)) {
      System.out.printf("\n (           grid.a[p.x][a.length - 1 - p.y])");
      System.out.printf("\n Particle type: %s", p.getClass().toString());
      System.out.printf("\n Velocity: velx:%.2f vely:%.2f", p.vel.x, p.vel.y);
      return false;
    }
    a[(int) p.loc.x][a.length - 1 - (int) p.loc.y] = p;
    return true;
  }

  /**
   * Moves a Particle from its current location to provided cartesian coordinates.
   * 
   * @param x
   * @param y
   * @param p
   */
  public void move(Particle p, int x, int y) {

    int py = (int) p.loc.y;
    int px = (int) p.loc.x;

    p.loc = new Point(x, y);

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
        // System.out.println(Granular.collide(p[0], p[1], 0));
        // p[0] <---> p[1]
        p[0].vel.x = 0;
        p[1].vel.x = 0;
        p[0].vel.y = 0;
        p[1].vel.y = 0;

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
      Particle p = spawnQueue.pollLast();

      if (p.oloc != null) {
        a[(int) p.oloc.x][a.length - 1 - (int) p.oloc.y] = null;
      }

      set(p);
    }

    forEachParticle((p) -> {

      Point prevTarget = p.loc;
      Point target = p.loc.add(p.vel.normalizedVect());

      for (int i = 0; i <= p.vel.magnitude(); i++) {

        if (test(target) && get(target) != p) {
          arena.add(new Particle[] { get(target), p });
          p.loc = prevTarget;
        }
        prevTarget = target;
        target = target.add(p.vel.normalizedVect());

      }
    });

    arena.process();

    forEachParticle((p) -> {

      // if (p.oloc != null) {
      p.oloc = p.loc;
      // }

      p.loc = p.loc.add(p.vel);

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

    HashSet<Particle> buff = new HashSet<>();
    Point p = new Point(0, 0);

    for (int x = 0; x < W; x++) {
      for (int y = 0; y < H; y++) {
        p.setLocation(x, y);
        if (test(p)) {
          if (!buff.contains(get(p))) {
            action.accept(get(p));
            buff.add(get(p));
          }
        }
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
      image.setRGB((int) particle.loc.x, H - 1 - (int) particle.loc.y, particle.displayColor.getRGB());
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
      // System.out.println(e);
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
  public boolean outOfBounds(Point p) {
    return (p.y >= H || p.y < 0 || p.x >= W || p.x < 0);
  }

}
