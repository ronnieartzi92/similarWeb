package SimilarWebHomeTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SiteURLPageViews {
  String siteUrl;
  HashMap<String, List<PageView>> pageViews;


  public SiteURLPageViews(PageView pageView) {
    this.siteUrl = pageView.siteUrl;
    this.pageViews = new HashMap<String, List<PageView>>() {{
      put(pageView.visitorID, Arrays.asList(pageView));}};
  }

}
