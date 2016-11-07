package com.abc.university.service

import akka.http.scaladsl.Http
import com.abc.university.config.BaseConfig
import com.abc.university.routes.FileRoutes
import com.abc.university.akka.AkkaService

object Boot extends App with FileRoutes with AkkaService with BaseConfig {
  Http().bindAndHandle(fileTransferRoute, httpHost, httpPort)
}
