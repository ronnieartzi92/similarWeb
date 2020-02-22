package SimilarWebHomeTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SiteURL {
  String siteUrl;
  HashMap<String, List<Session>> sessions;

  public SiteURL(PageView pageView) {
    Session session = new Session(pageView.ts, pageView.ts);
    this.siteUrl = pageView.siteUrl;
    List<Session> sessions = new ArrayList<Session>(){{add(session);}};
    this.sessions = new HashMap<String, List<Session>>() {{put(pageView.visitorID, sessions);}};
  }

  public SiteURL(String siteUrl, HashMap<String, List<Session>> sessions) {
    this.siteUrl = siteUrl;
    this.sessions = sessions;
  }

  public double findMedian(List<Long> list) {
    Collections.sort(list);
    int n = list.size();
    if (n % 2 != 0)
      return (double)list.get(n / 2);
    return (double)(list.get((n - 1) / 2) + list.get(n / 2)) / 2.0;
  }

}
