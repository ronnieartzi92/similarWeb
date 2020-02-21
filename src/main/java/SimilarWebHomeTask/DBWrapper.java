package SimilarWebHomeTask;

import java.util.List;

public interface DBWrapper {

  /**
   * This method will execute the query
   * "SELECT * FROM messages WHERE Status = "Accepted" limit" + bulkNum
   */
  List<SiteURL> queryBulkToProcess(int bulkSize);

  /**
   * This method will execute the insert query
   * "INSERT OR REPLACE into messages values (id, msg.status, msg.data);"
   */
  void insert(String id, SiteURL msg);

  /**
   * This method will execute the query
   * "SELECT status FROM messages WHERE msgId = " + id;"
   */
  SiteURL get(String id);

}
