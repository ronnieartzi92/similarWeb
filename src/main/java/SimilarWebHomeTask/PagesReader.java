package SimilarWebHomeTask;

import com.opencsv.CSVReader;
import java.io.FileReader;

public class PagesReader {
  private static final InMemoryDB inMemoryDb = InMemoryDB.getInstance();

  public static void readDataLineByLine(String file) {
    try {
      // Create an object of filereader
      // class with CSV file as a parameter.
      FileReader filereader = new FileReader(file);

      // create csvReader object passing
      // file reader as a parameter
      CSVReader csvReader = new CSVReader(filereader);
      String[] nextRecord;

      // we are going to read data line by line
      while ((nextRecord = csvReader.readNext()) != null) {
        PageView pageView = new PageView(
            nextRecord[0], nextRecord[1], nextRecord[2], Long.parseLong(nextRecord[3]));
        inMemoryDb.insert(pageView);
        inMemoryDb.insertToUsersTable(pageView);
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
    inMemoryDb.sessionsCreation();
  }
}
