package com.abc.university.ittest

import org.scalatest.{FlatSpec, Matchers}
import com.abc.university.client.ClientUtils
import scala.concurrent.duration._
import scala.concurrent.Await


class AbcSpec extends FlatSpec with Matchers {

  val fnSrcStudents = "/home/woody/data/boa_data/students.txt"
  val fnSrcCourses = "/home/woody/data/boa_data/courses.txt"
  val fnSrcEnrollments1 = "/home/woody/data/boa_data/course_enrollments1.txt"
  val fnSrcEnrollments2 = "/home/woody/data/boa_data/course_enrollments2.txt"

  val filenames = List(fnSrcStudents, fnSrcCourses, fnSrcEnrollments1, fnSrcEnrollments2)

  it should "be able to process abcUniversity files" in {
    val res =
      Await.result(ClientUtils.abcUniversityCommand(filenames), 10 seconds)

    val basis =
      "\n" +
      "PROBLEM 1: Top 5 students by GPA:\n" +
      "4.00 Kareem Johnson\n" +
      "3.75 Jaime Gonzalez\n" +
      "3.67 Natasha Baskov\n" +
      "3.67 Molly Hayes\n" +
      "3.50 Siva Mehta\n" +
      "\n" +
      "PROBLEM 2: Course with lowest average grade:\n" +
      "2.00 Accounting\n" +
      "\n" +
      "PROBLEM 3: Course with highest average grade:\n" +
      "3.00 Java Programming\n" +
      "\n" +
      "PROBLEM 4: Avg student GPA by year:\n" +
      "2.63 Freshman\n" +
      "2.75 Sophomore\n" +
      "2.18 Junior\n" +
      "2.27 Senior\n"

      assert(res === basis)
  }
}
