package com.abc.university.routes

import scala.concurrent.duration._
import scala.concurrent.{Future}
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.model.Multipart.FormData
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import akka.stream.scaladsl.Framing
import com.abc.university.sinks.SinkFunctions
import com.abc.university.shapes.Shapes._
import com.abc.university.stats.Stats


trait FileRoutes {

  def fileTransferRoute: Route = {
    pathPrefix("process") {
      pathPrefix("students") {
        pathEndOrSingleSlash {
          (post & entity(as[FormData])) {
            formData => processGradeAssociatesFileRoute(formData,
                                                        StudentShape,
                                                        SinkFunctions.processStudent,
                                                        Stats.putStudentData)
          } ~
          complete("process/students POST only")
        } ~
        complete("process/students path too long")
      } ~
      pathPrefix("courses") {
        pathEndOrSingleSlash {
          (post & entity(as[FormData])) {
            formData => processGradeAssociatesFileRoute(formData,
                                                        CourseShape,
                                                        SinkFunctions.processCourse,
                                                        Stats.putCourseData)
          } ~
          complete("process/courses POST only")
        } ~
        complete("process/courses path too long")
      } ~
      pathPrefix("enrollments") {
        pathEndOrSingleSlash {
          (post & entity(as[FormData])) {
            formData => processGradeAssociatesFileRoute(formData,
                                                        EnrollmentShape,
                                                        SinkFunctions.processEnrollment,
                                                        Stats.putEnrollmentData)
          } ~
          complete("process/enrollments POST only")
        } ~
          complete("process/enrollments path too long")
      } ~
      complete("path process undefined")
    } ~
    pathPrefix("enrollments") {
      pathEndOrSingleSlash {
        get {
          complete(200, Stats.getStats)
        } ~
        complete("students GET only")
      } ~
      complete("process/students path too long")
    } ~
    complete("path process incomplete")

  }



  def processGradeAssociatesFileRoute[T](formData: FormData,
                                         shape: FieldShape,
                                         processItem: String => Option[(String, T)],
                                         putData: Map[String, T] => Unit
                                        ): Route = {
    extractRequestContext { ctx =>
      implicit val materializer = ctx.materializer
      implicit val ec = ctx.executionContext

      val extractedData: Future[Map[String, Any]] =
        formData.parts.mapAsync[(String, Any)](1) {
          case bp: BodyPart if bp.name == "file" =>
            bp.entity.dataBytes
              .via(Framing.delimiter(ByteString(recordSeparator), maximumFrameLength=2048, allowTruncation=true))
              .runWith(Sink.fold(Map.empty[String, T]) { (ac, bs) =>
                processItem(bs.utf8String) match {
                  case Some((s, assoc)) =>
                    ac + (s -> assoc)
                  case None =>
                    ac
                }
              })
              .map{ m =>
                if (m.contains(shape.fields(0)))  putData(m -(shape.fields(0)))
                else                              putData(m)
                s"${bp.filename.fold("Unknown")(identity)} records processed " -> s" ${m.keys.toSeq.length.toString}"
              }

          case bp: BodyPart =>
            bp.toStrict(2.seconds).map(strict => bp.name -> strict.entity.data.utf8String )

        }.runFold(Map.empty[String, Any]) { (map, e) => map + e }

      complete( extractedData.map { data =>
          HttpResponse(StatusCodes.OK, entity = s"${data.mkString}")
        }
        .recover {
          case e: Exception =>
            HttpResponse(StatusCodes.InternalServerError, entity = s"Error in processing multipart form data due to ${e.getMessage}")
        }
      )
    }
  }
}
