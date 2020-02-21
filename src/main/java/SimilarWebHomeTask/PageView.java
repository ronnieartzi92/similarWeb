package SimilarWebHomeTask;

public class PageView {
  String visitorID;
  String siteUrl;
  String pageUrl;
  Long ts;

  public PageView(String visitorID, String siteUrl, String pageUrl, Long ts) {
    this.visitorID = visitorID;
    this.siteUrl = siteUrl;
    this.pageUrl = pageUrl;
    this.ts = ts;
  }
}
