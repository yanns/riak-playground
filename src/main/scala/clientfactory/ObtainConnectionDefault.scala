package clientfactory

import com.basho.riak.client.RiakFactory
import scala.util.{Failure, Success, Try}

object ObtainConnectionDefault extends App {

  object connection {
    val address = "127.0.0.1"
    val httpPort = 8098
    val protoBufferPort = 8087
  }

  // Riak HTTP client with defaults
  val myDefaultHttpClient = RiakFactory.httpClient()

  Try {
    myDefaultHttpClient.ping()
  } match {
    case Success(_) => println(s"connection with http client OK")
    case Failure(e) => println(s"error while connecting with http client: ${e.getMessage}")
  }


  // Riak HTTP client using supplied URL
  val httpConnectionUrl = s"http://${connection.address}:${connection.httpPort}/riak"
  val myHttpClient = RiakFactory.httpClient(httpConnectionUrl)

  Try {
    myHttpClient.ping()
  } match {
    case Success(_) => println(s"riak is running on '$httpConnectionUrl'")
    case Failure(e) => println(s"error while connecting to riak on '$httpConnectionUrl': ${e.getMessage}")
  }



  // Riak Protocol Buffers client with defaults
  val myDefaultPbClient = RiakFactory.pbcClient()

  Try {
    myDefaultPbClient.ping()
  } match {
    case Success(_) => println(s"connection with proto buffers OK")
    case Failure(e) => println(s"error while connecting with proto buffers with defaults: ${e.getMessage}")
  }


  // Riak Protocol Buffers client with supplied IP and Port
  val myPbClient = RiakFactory.pbcClient(connection.address, connection.protoBufferPort)

  Try {
    myPbClient.ping()
  } match {
    case Success(_) => println(s"connection with proto buffers with supplied IP '${connection.address}' and Port '${connection.protoBufferPort}' OK")
    case Failure(e) => println(s"error while connecting with proto buffers with supplied IP '${connection.address}' and Port '${connection.protoBufferPort}': ${e.getMessage}")
  }

  myDefaultHttpClient.shutdown()
  myHttpClient.shutdown()
  myDefaultPbClient.shutdown()
  myPbClient.shutdown()
}
