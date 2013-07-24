package clientfactory

import com.basho.riak.client.RiakFactory
import scala.util.{Failure, Success, Try}
import com.basho.riak.client.raw.http.{HTTPClientConfig, HTTPClusterConfig}
import com.basho.riak.client.raw.pbc.{PBClientConfig, PBClusterConfig}

object ObtainConnectionCluster extends App {

  object connection {
    val httpPort = 8098
    val protoBufferPort = 8087
    val maxConnections = 50
    // first node must be reachable for ping
    val cluster = Array("127.0.0.1", "192.168.1.10")
  }

  val httpClusterConfig = new HTTPClusterConfig(connection.maxConnections)
  httpClusterConfig.addHosts(HTTPClientConfig.defaults(), connection.cluster:_*)

  // Riak HTTP client with cluster
  val httpClient = RiakFactory.newClient(httpClusterConfig)

  Try {
    httpClient.ping()
  } match {
    case Success(_) => println(s"connection with http client OK")
    case Failure(e) => println(s"error while connecting with http client: ${e.getMessage}")
  }

  // Riak Protocol Buffers client with defaults
  val pbClusterConfig = new PBClusterConfig(connection.maxConnections)
  pbClusterConfig.addHosts(PBClientConfig.defaults(), connection.cluster:_*)
  val pbClient = RiakFactory.newClient(pbClusterConfig)

  Try {
    pbClient.ping()
  } match {
    case Success(_) => println(s"connection with proto buffers OK")
    case Failure(e) => println(s"error while connecting with proto buffers with defaults: ${e.getMessage}")
  }

  httpClient.shutdown()
  pbClient.shutdown()
}
