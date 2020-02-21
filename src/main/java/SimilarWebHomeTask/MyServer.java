package SimilarWebHomeTask;

import ratpack.handling.Context;
import ratpack.path.PathTokens;
import ratpack.server.RatpackServer;
import java.util.logging.Logger;

class MyServer implements Runnable {
  private static final InMemoryDB inMemoryDb = InMemoryDB.getInstance();
  private static final Logger logger = Logger.getLogger(Process.class.getName());

  private void getNumSessions(Context ctx) {
    PathTokens pathTokens = ctx.getPathTokens();
    String siteUrl = pathTokens.get("siteUrl");
    Integer numSessions = inMemoryDb.getNumSessions(siteUrl);
    ctx.getResponse().send("the number of sessions for url " + siteUrl + " is " + numSessions.toString());
  }

  private void getMedianSessionLength(Context ctx) {
    PathTokens pathTokens = ctx.getPathTokens();
    String siteUrl = pathTokens.get("siteUrl");
    Double medianSessionsLength = inMemoryDb.getMedianSessionsLength(siteUrl);
    ctx.getResponse().send("the median sessions length for url " + siteUrl + " is " + medianSessionsLength.toString());
  }

  private void getNumUniqueVisitedSites(Context ctx) {
    PathTokens pathTokens = ctx.getPathTokens();
    String visitorId = pathTokens.get("visitorId");
    Integer numUniqueVisitedSite = inMemoryDb.getNumUniqueVisitedSite(visitorId);
    ctx.getResponse().send("the number of unique visited sites for visitor " + visitorId + " is " + numUniqueVisitedSite.toString());
  }

  @Override
  public void run() {
    try {
      RatpackServer.start(server -> server
          .handlers(chain -> chain
              .get(ctx -> ctx.render("Hey, please submit your query"))
              .get("getNumSessions/:siteUrl", ctx -> ctx.byMethod(m -> m.get(() -> getNumSessions(ctx))))
              .get("getSessionMedianLength/:siteUrl", ctx -> ctx.byMethod(m -> m.get(() -> getMedianSessionLength(ctx))))
              .get("getNumUniqueVisitedSites/:visitorId", ctx -> ctx.byMethod(m -> m.get(() -> getNumUniqueVisitedSites(ctx)))
              )
          ));
    } catch (Exception e) {
      logger.info("Exception " + e + "running ratPackServer");
      e.printStackTrace();
    }
  }
}