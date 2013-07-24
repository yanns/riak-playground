package javadriver.fetchdata.hardway

import java.util
import com.basho.riak.client.convert.RiakKey

case class NameScorePair(name: String, score: Int) extends Comparable[NameScorePair] {
  def compareTo(t: NameScorePair): Int = {
    if (this.score < t.score) {
      -1
    } else if (this.score > t.score) {
      1
    } else if (this.name.equalsIgnoreCase(t.name)) {
      0
    } else {
      -1
    }
  }
}

class GameLeaderboard(
   /*
    * The @RiakKey annotation allows the StoreObject to extract the key you wish to use
    * from your POJO. If you're using the default JSONConverter, this is excluded
    * from serialization
    */
    @RiakKey val gameName: String,
    private[GameLeaderboard] val scoreList: java.util.TreeSet[NameScorePair] = new java.util.TreeSet()
  ) {

  def addScore(s: NameScorePair) {
    scoreList.add(s)
    if (scoreList.size > 5) {
      scoreList.pollFirst()
    }
  }

  def addScores(scores: util.Collection[NameScorePair]) {
    scoreList.addAll(scores)
    while (scoreList.size() > 5) {
      scoreList.pollFirst()
    }
  }

  def getScoreList = new util.ArrayList[NameScorePair](scoreList.descendingSet())
}
