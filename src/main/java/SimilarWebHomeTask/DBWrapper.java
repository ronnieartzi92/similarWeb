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
   * "SELECT COUNT(*) FROM visitorTable WHERE visitorId = " + visitorId;
   */
  Integer getNumUniqueVisitedSite(String visitorId);

  /**
   * This method will execute the query
   * "SELECT COUNT(*) FROM sitesTable WHERE visitorId = " + visitorId + " GROUP BY siteUrl"
   */
  Integer getNumUniqueVisitedSiteInPlace(String visitorId);

}
