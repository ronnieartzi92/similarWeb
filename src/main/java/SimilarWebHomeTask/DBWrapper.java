package SimilarWebHomeTask;

public interface DBWrapper {

  /**
   * This method will execute the query
   * "SELECT COUNT(*) FROM sitesTable GROUP BY siteUrl"
   */
  Integer getNumSessions(String siteUrl);

  /**
   * This method will execute the query
   * "SELECT tsStart, tsLast FROM sitesTable WHERE siteUrl = " + siteUrl;
   * then will go over each row and calculate the Session length, will sort it and return the median.
   */
  Double getMedianSessionsLength(String siteUrl);

  /**
   * This method will execute the query
   * "SELECT COUNT(*) FROM sitesTable WHERE visitorId = " + visitorId + " GROUP BY siteUrl"
   */
  Integer getNumUniqueVisitedSite(String visitorId);

  /**
   * This method will execute the insert query
   * "INSERT INTO pageViewsTable (pageViewId, siteUrl, userId, pageUrl, timestamp)
   * VALUES (id, pageView.visitorID, pageView.siteUrl, pageView.pageUrl, pageView.ts);
   */
  void insert(PageView pageView);

}
