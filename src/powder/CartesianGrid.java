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

        //System.out.printf("Out of bounds at (%d, %d)", x, y);
        //System.exit(1);
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


  //public static void main(String[] args) {
//    
    //String[][] asdf = new String[90][90];
    //asdf[0][0] = "test";
//    
    //CartesianGrid<String> cg = new CartesianGrid<String>(asdf);
//
    //cg.set(0, 50, "hohoho");
//
    //cg.set(, y, element)
    //cg.get(0);
    //System.out.println(cg.get(0,50));
    //System.out.println(cg.size());
    //System.out.println(cg.W);
//
//
  //}


}
