package javadriver.fetchdata.hardway

import com.basho.riak.client.cap.ConflictResolver
import java.util

class GameLeaderboardResolver extends ConflictResolver[GameLeaderboard] {

  /*
   * Riak hands us a list of GameLeaderboard objects. Our job is to reconcile
   * those objects and return a single, resolved GameLeaderboard
   *
   * In this example, the logic is pretty straightforard. in our GameLeaderboard
   * class we created a addScores(Collection<NameScorePair>) method that will do the
   * heavy lifting for us. By adding all the lists into one GameLeaderboard
   * via that method, we end up with the top 5 scores from all the siblings
   *
   * Worth noting is that your ConflictResolver is *always* called, even if
   * there are no siblings, or even if there is no object in Riak
   */
  def resolve(siblings: util.Collection[GameLeaderboard]): GameLeaderboard = {
    if (siblings.size() > 1) {
      // We have siblings, need to resolve them
      val i = siblings.iterator()

      val gameLeaderboard = i.next()
      val resolvedLeaderBoard = new GameLeaderboard(gameName = gameLeaderboard.gameName)
      resolvedLeaderBoard.addScores(gameLeaderboard.getScoreList)

      while (i.hasNext) {
        resolvedLeaderBoard.addScores(i.next().getScoreList)
      }

      resolvedLeaderBoard

    } else if (siblings.size() == 1) {
      // Only one object - just return it
      siblings.iterator().next()
    } else {
      // No object returned - return null
      null
    }
  }
}
