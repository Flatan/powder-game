package powder;

public class Logger{

  static void log(String format, Object... args) {

    // Super basic in case we want to add more actions later
    System.out.printf(format + "\n", args);


  }




}
