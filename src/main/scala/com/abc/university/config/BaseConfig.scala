package com.abc.university.config

import java.io.File
import com.typesafe.config.ConfigFactory

trait BaseConfig {
  val config = ConfigFactory.load()
  val appConfig =  config.getConfig("app")
  val httpConfig = config.getConfig("http")
  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")
}
