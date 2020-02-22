package SimilarWebHomeTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Attachment {
  private final HashMap<String, SiteURLPageViews> pageViewsTable;
  private final HashMap<String, SiteURL> sitesTable;
  private static final long sessionDefinition = TimeUnit.MINUTES.toSeconds(30);

  private Attachment() {
    this.pageViewsTable = new HashMap<>();
    this.sitesTable = new HashMap<>();
  }

  public void sessionsCreation() {
    for (String siteUrl : pageViewsTable.keySet()) {
      HashMap<String, List<Session>> sessions = new HashMap<>();
      for (List<PageView> pageViewList : pageViewsTable.get(siteUrl).pageViews.values()) {
        pageViewList.sort(Comparator.comparing(s -> s.ts));
        for (PageView pv : pageViewList) {
          addPV(sessions, pv);
        }
      } sitesTable.put(siteUrl, new SiteURL(siteUrl, sessions));
    }
  }

  public void addPV(HashMap<String, List<Session>> sessions, PageView pv){
    List<Session> sessionsList = new ArrayList<>();
    if (!sessions.containsKey(pv.visitorID)) {
      sessionsList.add(new Session(pv));
    } else {
      sessionsList = sessions.get(pv.visitorID);
      Session session = sessionsList.get(sessionsList.size() - 1);
      if (pv.ts - session.tsLast <= sessionDefinition) {
        sessionsList.set(sessionsList.size() - 1, session.addPV(pv));
      } else {
        sessionsList.add(new Session(pv));
      }
    } sessions.put(pv.visitorID, sessionsList);
  }
}
