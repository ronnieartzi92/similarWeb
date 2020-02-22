package SimilarWebHomeTask;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.logging.Logger;

public class PagesReader {
  private static final InMemoryDB inMemoryDb = InMemoryDB.getInstance();
  private static final Logger logger = Logger.getLogger(Process.class.getName());

  private static void readDataLineByLine(String file) {
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
      logger.info("Exception " + e + "reading CSV file");
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    for (String file : args) {
      readDataLineByLine(file);
    }
  }
}
