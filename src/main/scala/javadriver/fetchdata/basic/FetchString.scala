package fetchdata.basic

import com.basho.riak.client.RiakFactory

object FetchString extends App {

  val riakClient = RiakFactory.httpClient()
  val myBucket = riakClient.fetchBucket("TestBucket").execute()

  val myData = "This is my data"
  myBucket.store("TestKey", myData).execute()

  val myObject = myBucket.fetch("TestKey").execute()
  println(myObject.getValueAsString)

  riakClient.shutdown()
}
