package SimilarWebHomeTask;

import com.opencsv.CSVReader;
import java.io.FileReader;

public class PagesReader {
  private static final InMemoryDB inMemoryDb = InMemoryDB.getInstance();

  public static void readDataLineByLine(String file) {
    try {
      FileReader filereader = new FileReader(file);
      CSVReader csvReader = new CSVReader(filereader);
      String[] nextRecord;

      while ((nextRecord = csvReader.readNext()) != null) {
        PageView pageView = new PageView(
            nextRecord[0], nextRecord[1], nextRecord[2], Long.parseLong(nextRecord[3]));
        inMemoryDb.insert(pageView);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    for (String file : args) {
      readDataLineByLine(file);
    }
  }
}
