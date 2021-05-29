package powder;

import java.util.AbstractCollection;
import java.util.Iterator;

class CartesianGrid<T> extends AbstractCollection<T> {

  private final T[][] a;
  public final int W;
  public final int H;
  
  CartesianGrid(T[][] array) {

    a = array;
    W = a.length;
    H = a[0].length;
    
  }

  public T get(int x, int y) {
      

      try {

        return a[x][a.length-1 - y];

      } catch (Exception e) {

        return null;
      }

  }

  public void set(int x, int y, T element) {

      a[x][a.length-1 - y] = element;
  }

  @Override
  public int size() {
      return (a.length * a[0].length);
  }

  @Override
  public Iterator<T> iterator() {
      return null;
  }

}
