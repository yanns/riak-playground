package javadriver.fetchdata.hardway

import com.basho.riak.client.cap.Mutation

class GameLeaderboardMutation(val nsp: NameScorePair) extends Mutation[GameLeaderboard] {

  /*
   * And at the heart of things is this method. After the data in Riak has
   * been converted to GameLeaderboard Objects and any siblings resolved,
   * Mutation.apply() is called and it is where you will do any and all modifications
   *
   * Here we add the NameScorePair we passed to the constructor to the
   * GameLeaderboard object. After this our modified data will be stored back
   * to Riak
   */
  def apply(original: GameLeaderboard): GameLeaderboard = {
    original.addScore(nsp)
    original
  }
}
