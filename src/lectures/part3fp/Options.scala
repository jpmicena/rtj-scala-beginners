package lectures.part3fp

import scala.util.Random

object Options extends App {
  val myFirstOption: Option[Int] = Some(4)
  val noOption: Option[Int] = None

  println(myFirstOption)

  // unsafe APIs
  def unsafeMethod(): String = null
  // val result = Some(unsafeMethod()) // WRONG
  val result = Option(unsafeMethod()) // Some or none
  println(result)


  // chained methods
  def backupMethod(): String = "A valid result"
  val chainedResult = Option(unsafeMethod()).orElse(Option(backupMethod()))

  // design unsafe APIs
  def betterUnsafeMethod(): Option[String] = None
  def betterBackupMethod(): Option[String] = Some("A valid result")

  val betterChainedResult = betterUnsafeMethod().orElse(betterBackupMethod())

  // functions on Options
  println(myFirstOption.isEmpty)
  println(myFirstOption.get) // UNSAFE - do not use this

  // map, flatMap, filter
  println(myFirstOption.map(_ * 2))
  println(myFirstOption.filter(_ > 10))
  println(myFirstOption.flatMap(x => Option(x * 10)))

  // for-comprehensions

  /*
      Exercise
   */

  val config: Map[String, String] = Map(
    // fetched from elsewhere
    "host" -> "176.45.36.1",
    "port" -> "80"
  )

  class Connection {
    def connect = "Connected"
  }
  object Connection {
    val random = new Random(System.nanoTime())

    def apply(host: String, port: String): Option[Connection] =
      if (random.nextBoolean()) Some(new Connection)
      else None
  }

  // try to establish a connection, if so - print the connect method

  val host: Option[String] = config.get("host")
  val port: Option[String] = config.get("port")
  val connection: Option[Connection] = host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))
  val connectionStatus = connection.map(c => c.connect)
  //println(connectionStatus)
  //connectionStatus.foreach(println)

  config.get("host")
    .flatMap(host => config.get("port")
      .flatMap(port => Connection(host, port)))
      .map(connection => connection.connect)
    //.foreach(println)

  // for comprehension
  val forConnectionStatus = for {
    host <- config.get("host")
    port <- config.get("port")
    connection <- Connection(host, port)
  } yield connection.connect
  forConnectionStatus.foreach(println)

}