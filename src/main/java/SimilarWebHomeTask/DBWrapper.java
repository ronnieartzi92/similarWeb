package SimilarWebHomeTask;

public interface DBWrapper {

  Integer getNumSessions(String siteUrl);


  Double getMedianSessionsLength(String siteUrl);


  Integer getNumUniqueVisitedSite(String visitorId);

}
