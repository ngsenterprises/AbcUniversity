package com.abc.university.client

import java.io.File

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{Multipart, _}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{FileIO, Source}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.abc.university.config.BaseConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object ClientUtils extends BaseConfig {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  //val urlBase = "http://woody-OptiPlex-780:8080"
  val urlBase = "http://127.0.0.1:8080"

  def createEntity(file: File): Future[RequestEntity] = {
    println(s"createEntity: ${file.getPath}")
    require(file.exists())
    val formData =
      Multipart.FormData(
        Source.single(
          Multipart.FormData.BodyPart(
            "file",
            HttpEntity(MediaTypes.`application/octet-stream`,
              file.length(),
              FileIO.fromPath(file.toPath, chunkSize = 100000)), // the chunk size here is currently critical for performance
            Map("filename" -> file.getName))))
    Marshal(formData).to[RequestEntity]
  }

  def createPostRequest(target: Uri, file: File): Future[HttpRequest] =
    createEntity(file).map {
      case e => HttpRequest(HttpMethods.POST, uri = target, entity = e)
    }

  def post(fileName: String, url: String): Future[String] = {
    try {
      for {
        req ← createPostRequest(url, new File(fileName))
        response ← Http().singleRequest(req)
        responseBodyAsString ← Unmarshal(response).to[String]
      } yield responseBodyAsString

    } catch {
      case e: Throwable ⇒ Future.failed(e)
    }

  }

  def get(url: String): Future[String] = {
    Future(scala.io.Source.fromURL(url).mkString)
  }

  def abcUniversityCommand(fileNames: Seq[String]): Future[String] = {
    require(fileNames.length == 4)
    for {
        res1 <- ClientUtils.post(fileNames(0), ClientUtils.urlBase + "/process/students")
        res2 <- ClientUtils.post(fileNames(1), ClientUtils.urlBase + "/process/courses")
        res3 <- ClientUtils.post(fileNames(2), ClientUtils.urlBase + "/process/enrollments")
        res4 <- ClientUtils.post(fileNames(3), ClientUtils.urlBase +"/process/enrollments")
        res5 <- ClientUtils.get(ClientUtils.urlBase +"/enrollments")
      } yield (res5)
  }

}
