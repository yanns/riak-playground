package clientfactory

import com.basho.riak.client.RiakFactory
import scala.util.{Failure, Success, Try}
import com.basho.riak.client.raw.http.HTTPClientConfig
import com.basho.riak.client.raw.pbc.PBClientConfig

object ObtainConnectionSuppliedConfiguration extends App {

  object connection {
    val address = "127.0.0.1"
    val httpPort = 8098
    val protoBufferPort = 8087
  }

  // http cllient
  val httpClientConfig = new HTTPClientConfig.Builder().withPort(connection.httpPort).withMaxConnections(20).build()
  val myHttpClient = RiakFactory.newClient(httpClientConfig)

  Try {
    myHttpClient.ping()
  } match {
    case Success(_) => println(s"http client on port ${connection.httpPort} OK")
    case Failure(e) => println(s"error with http client on port ${connection.httpPort}: ${e.getMessage}")
  }


  // proto buffer client

  val protoBufConfig = new PBClientConfig.Builder().withConnectionTimeoutMillis(3000).withHost(connection.address).build();
  val myPbClient = RiakFactory.newClient(protoBufConfig)

  Try {
    myPbClient.ping()
  } match {
    case Success(_) => println(s"connection with proto buffers with supplied port '${connection.protoBufferPort}' OK")
    case Failure(e) => println(s"error while connecting with proto buffers with supplied port '${connection.protoBufferPort}': ${e.getMessage}")
  }

  myHttpClient.shutdown()
  myPbClient.shutdown()
}
