package SimilarWebHomeTask;

public class Main {

  public static void main(String[] args) {
    PagesReader.main(args);
    Runnable server = new MyServer();
    server.run();
  }
}
