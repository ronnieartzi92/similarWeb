package SimilarWebHomeTask;

public class Session {
  String visitorId;
  Long tsStart;
  Long tsLast;

  public Session(PageView pageView) {
    this.visitorId = pageView.visitorID;
    this.tsStart = pageView.ts;
    this.tsLast = pageView.ts;
  }

  public Session(Long tsStart, Long tsLast) {
    this.tsStart = tsStart;
    this.tsLast = tsLast;
  }

  public Session addPV(PageView pageView) {
    this.tsLast = pageView.ts;
    return this;
  }
}
