package com.abc.university.stats

import com.abc.university.shapes.{Student, Course, Enrollment, GradeAssociate}
import com.abc.university.shapes.Shapes._
import scala.collection.mutable.ListBuffer


object Stats {

  def putCourseData(m : Map[String, Course]): Unit = {
    courses ++= m
  }
  def putStudentData(m : Map[String, Student]): Unit = {
    students ++= m
  }
  def putEnrollmentData(data: Map[String, Enrollment]):Unit = {}
  def putEnrollmentItem(data: Enrollment):Unit = {
    studentStats += (data.studentId -> (studentStats.getOrElse(data.studentId, List.empty[Double]) :+ data.grade))
    courseStats += (data.courseId -> (courseStats.getOrElse(data.courseId, List.empty[Double]) :+ data.grade))
    if (students.contains(data.studentId)) {
      val student = students(data.studentId)
      yearStats += (student.year -> (yearStats.getOrElse(student.year, List.empty[Double]) :+ data.grade))
    }
  }
  def getStats: String = {
    var buf = new StringBuilder("\nPROBLEM 1: Top 5 students by GPA:\n")

    buf ++=
    studentStats.foldLeft(List.empty[(String, Double)]) { (ac, si) => ac :+ (si._1, si._2.sum/si._2.length) }
    .sortWith( _._2 > _._2)
    .take(5)
    .foldLeft(""){ (ac, si) =>
      if (students.contains(si._1)) ac + f"${si._2}%1.2f ${students(si._1).name}%s\n"
      else                          ac
    }

    val cStats =
      courseStats.foldLeft(ListBuffer.empty[(String, Double)]) { (ac, ci) => ac :+ (ci._1, ci._2.sum/ci._2.length) }
      .sortWith( _._2 > _._2)

    buf ++= "\nPROBLEM 2: Course with lowest average grade:\n"
    if (0 < cStats.length && courses.contains(cStats.last._1)) {
      buf ++= f"${cStats.last._2}%1.2f ${courses(cStats.last._1).name}%s\n\n"
    }
    buf ++= "PROBLEM 3: Course with highest average grade:\n"

    if (0 < cStats.length && courses.contains(cStats.head._1)) {
      buf ++= f"${cStats.head._2}%1.2f ${courses(cStats.head._1).name}%s\n\n"
    }
    buf ++= "PROBLEM 4: Avg student GPA by year:\n"

    val ystats =
      yearStats.toSeq.foldLeft((0.0d, 0.0d, 0.0d, 0.0d)) { (ac, yi) =>
        val gpa = if (0 < yi._2.length) yi._2.sum/yi._2.length.toDouble else 0.0d
        if (yi._1.compareToIgnoreCase(Freshman.fields.head) == 0) (gpa, ac._2, ac._3, ac._4)
        else if (yi._1.compareToIgnoreCase(Sophomore.fields.head) == 0) (ac._1, gpa, ac._3, ac._4)
        else if (yi._1.compareToIgnoreCase(Junior.fields.head) == 0) (ac._1, ac._2, gpa, ac._4)
        else if (yi._1.compareToIgnoreCase(Senior.fields.head) == 0) (ac._1, ac._2, ac._3, gpa)
        else  ac
      }

    buf ++= f"${ystats._1}%1.2f ${Freshman.fields.head}%s\n"
    buf ++= f"${ystats._2}%1.2f ${Sophomore.fields.head}%s\n"
    buf ++= f"${ystats._3}%1.2f ${Junior.fields.head}%s\n"
    buf ++= f"${ystats._4}%1.2f ${Senior.fields.head}%s\n"

    buf.toString
  }

  def getGrade(grade: String): Option[Double] = grade match {
    case "A" => Some(4.0d)
    case "B" => Some(3.0d)
    case "C" => Some(2.0d)
    case "D" => Some(1.0d)
    case "F" => Some(0.0d)
    case _ => None
  }

  val courses = scala.collection.mutable.Map.empty[String, Course]
  val students = scala.collection.mutable.Map.empty[String, Student]

  val studentStats = scala.collection.mutable.Map.empty[String, List[Double]]
  val courseStats = scala.collection.mutable.Map.empty[String, List[Double]]
  val yearStats = scala.collection.mutable.Map.empty[String, List[Double]]

}

