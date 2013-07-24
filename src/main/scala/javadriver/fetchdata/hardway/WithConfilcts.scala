package javadriver.fetchdata.hardway

import scala.util.Random
import com.basho.riak.client.RiakFactory
import scala.collection.JavaConversions._

object WithConfilcts extends App {

  // We need some data, of course
  val playerNames = Array("Steve","Brian","Bob")
  val generator = new Random()
  val gl = new GameLeaderboard(gameName = "SuperCoolGame")

  for (i <- 0 to 5) {
    val nsp = new NameScorePair(playerNames((i + 3) % 3), generator.nextInt(100))
    gl.addScore(nsp)
  }

  // Store our initial leaderboard in Riak
  val myDefaultHttpClient = RiakFactory.httpClient()
  val b = myDefaultHttpClient.createBucket("demo_bucket").allowSiblings(true).execute()
  b.store(gl).withResolver(new GameLeaderboardResolver()).execute()

  val g2 = b.fetch("SuperCoolGame", classOf[GameLeaderboard])
    .withResolver(new GameLeaderboardResolver())
    .execute()

  // output the result
  for (n <- g2.getScoreList) {
    println(s"${n.name} ${n.score}")
  }
  println()


  /*
   * Now that we have a leaderboard in Riak, lets modify it!
   * This simulates a new name/score pair coming in, and we're going
   * to modify the leaderboard in Riak using the GamLeaderboardMutation
   * We know our sample data only has scores to 100, so using 1000 ensures
   * we'll modify the object
   */
  val nsp = new NameScorePair("John", 1000)
  val glbm = new GameLeaderboardMutation(nsp)

  /* Note that as mentioned in the cookbook, the GameLeaderboard object
   * passed to Bucket.store() is discarded after the type is inferred
   * and the key extracted - all modification is done by your Mutation
   *
   * Note also that we're calling returnBody(true) in order to get
   * the current data back
   */

  val g3 = b.store(new GameLeaderboard("SuperCoolGame"))
    .withMutator(glbm)
    .withResolver(new GameLeaderboardResolver())
    .returnBody(true)
    .execute()

  // output the result
  for (n <- g3.getScoreList) {
    println(s"${n.name} ${n.score}")
  }

  myDefaultHttpClient.shutdown()
}
