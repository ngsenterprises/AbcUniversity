package com.abc.university.akka

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import scala.concurrent.ExecutionContextExecutor

trait AkkaService {
  implicit val system: ActorSystem = ActorSystem()
  implicit def executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = ActorMaterializer()
}
