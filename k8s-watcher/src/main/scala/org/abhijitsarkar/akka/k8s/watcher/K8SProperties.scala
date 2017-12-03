package org.abhijitsarkar.akka.k8s.watcher

import java.net.URI

/**
  * @author Abhijit Sarkar
  */
case class K8SProperties(
                          baseUrl: String = "http://localhost:9000",
                          namespace: String = "default",
                          certFile: Option[String] = None,
                          apiTokenFile: Option[String] = None,
                          apiToken: Option[String] = None,
                          apps: List[String] = Nil
                        ) {
  private val u = URI.create(baseUrl)

  val host = u.getHost
  val port = u.getPort
}