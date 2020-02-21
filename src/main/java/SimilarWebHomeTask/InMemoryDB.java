package SimilarWebHomeTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class InMemoryDB implements DBWrapper{
  private final ConcurrentHashMap<String, SiteURLPageViews> pageViewsTable;
  private final ConcurrentHashMap<String, SiteURL> sitesTable;
  private final ConcurrentHashMap<String, HashSet<String>> visitorTable;
  private static final InMemoryDB inMemoryDb = new InMemoryDB();
  private static final long sessionDefinition = TimeUnit.MINUTES.toSeconds(30);

  private InMemoryDB() {
    this.pageViewsTable = new ConcurrentHashMap<>();
    this.visitorTable = new ConcurrentHashMap<>();
    this.sitesTable = new ConcurrentHashMap<>();
  }

  public static InMemoryDB getInstance() {
    return inMemoryDb;
  }

  public Integer getNumSessions(String siteUrl) {
    SiteURL siteURL = sitesTable.get(siteUrl);
    return Objects.nonNull(siteURL) ?
        sitesTable.get(siteUrl).sessions.values().stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList()).size() : 0;
  }

  public Double getMedianSessionsLength(String siteUrl) {
    SiteURL siteURL = sitesTable.get(siteUrl);
    return Objects.nonNull(siteURL) ? siteURL.getMedianSessionLength() : 0;
  }

  public Integer getNumUniqueVisitedSite(String visitorId) {
    HashSet<String> sites = visitorTable.get(visitorId);
    return Objects.nonNull(sites) ? visitorTable.get(visitorId).size() : 0;
  }

  public void insert(PageView pageView) {
    SiteURLPageViews siteURLPV;
    if (!pageViewsTable.containsKey(pageView.siteUrl)) {
      siteURLPV = new SiteURLPageViews(pageView);
    } else {
      siteURLPV = pageViewsTable.get(pageView.siteUrl);
      if (!siteURLPV.pageViews.containsKey(pageView.visitorID)) {
        siteURLPV.pageViews.put(pageView.visitorID, new ArrayList<PageView>() {{ add(pageView); }});
      } else {
        siteURLPV.pageViews.put(pageView.visitorID,
            new ArrayList<PageView>(siteURLPV.pageViews.get(pageView.visitorID)){{add(pageView);}});
      }
    } pageViewsTable.put(pageView.siteUrl, siteURLPV);
//    insertSiteTable(pageView);
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

  public void insertSiteTable(PageView pageView) {
    SiteURL siteURL;
    if (!sitesTable.containsKey(pageView.siteUrl)) {
      siteURL = new SiteURL(pageView);
    } else {
      siteURL = sitesTable.get(pageView.siteUrl);
      if (!siteURL.sessions.containsKey(pageView.visitorID)) {
        List<Session> sessions = new ArrayList<Session>(){{add(new Session(pageView));}};
        siteURL.sessions.put(pageView.visitorID, sessions);
      } else {
        siteURL = inTableHandler(siteURL, pageView);
      }
    } sitesTable.put(pageView.siteUrl, siteURL);
  }

  public SiteURL inTableHandler(SiteURL siteURL, PageView pageView) {
    List<Session> sessions = siteURL.sessions.get(pageView.visitorID);
    Session session = sessions.get(sessions.size()-1);
    long tsDiff = pageView.ts - session.tsLast;
    if (tsDiff < 0) {
      sessions = overLapsTimestamps(pageView, sessions);
      sessions.sort(Comparator.comparing(s -> s.tsLast));
    } else if (tsDiff <= sessionDefinition) {
      sessions.set(sessions.size()-1, session.addPV(pageView));
    } else {
      sessions.add(new Session(pageView));
    }
    siteURL.sessions.put(pageView.visitorID, sessions);
    return siteURL;
  }

  public List<Session> overLapsTimestamps(PageView pageView, List<Session> sessions) {
    for (int i = 0; i < sessions.size(); i++) {
      Long startDiff = pageView.ts - sessions.get(i).tsStart;
      Long endDiff = pageView.ts - sessions.get(i).tsLast;
      Session session = sessions.get(i);
      if (Math.abs(endDiff) < sessionDefinition || Math.abs(startDiff) < sessionDefinition) {
        session.tsLast = endDiff < 0 ? session.tsLast : pageView.ts;
        session.tsStart = startDiff < 0 ? pageView.ts : session.tsStart;
        if (session.tsLast.equals(pageView.ts) && i < sessions.size()-1) {
          if (sessions.get(i + 1).tsStart - session.tsLast < sessionDefinition) {
            session.tsLast = sessions.get(i + 1).tsLast;
            setAndRemove(sessions, session, i, +1);
          }
        }
        else if (session.tsStart.equals(pageView.ts) && i > 0) {
          if ((session.tsStart - sessions.get(i-1).tsLast) < sessionDefinition) {
            session.tsStart = sessions.get(i-1).tsStart;
            setAndRemove(sessions, session, i, -1);
          }
        } else {
          sessions.set(i, session);
        } return sessions;
      }
    }
    sessions.add(new Session(pageView));
    return sessions;
  }

  public void setAndRemove(List<Session> sessions, Session session, int index, int indent) {
    sessions.set(index, session);
    sessions.remove(index + indent);
  }

  public void insertToUsersTable(PageView pageView) {
    HashSet<String> sites;
    if (!visitorTable.containsKey(pageView.visitorID)) {
      sites = new HashSet<String>() {{add(pageView.siteUrl);}};
    } else {
      sites = visitorTable.get(pageView.visitorID);
      sites.add(pageView.siteUrl);
    } visitorTable.put(pageView.visitorID, sites);
  }

}
