package com.abc.university.sinks

import com.abc.university.shapes._
import com.abc.university.shapes.Shapes._
import com.abc.university.stats.Stats

object SinkFunctions {

  def processCourse(line: String): Option[(String, Course)] = {
    val arr = line.split(fieldSeparator)
    if (arr.length < CourseShape.fields.length) None else
      Some( (arr(0).trim, Course(arr(0).trim, arr(1).trim, arr(2).trim, arr(3).trim)) )
  }
  def processStudent(line: String): Option[(String, Student)] = {
    val arr = line.split(fieldSeparator)
    if (arr.length < StudentShape.fields.length) None else
      Some( (arr(0).trim, Student(arr(0).trim, arr(1).trim, arr(2).trim)) )
  }
  def processEnrollment(line: String): Option[(String, Enrollment)] = {
    val arr = line.split(fieldSeparator)
      if (arr.length < EnrollmentShape.fields.length)
        None
      else
        Stats.getGrade(arr(2)) match {
          case Some(d) =>
            val ed = Enrollment(arr(0).trim, arr(1).trim, d)
            Stats.putEnrollmentItem(ed)
            Some((arr(0).trim + ":" + arr(1).trim, ed))
          case None => None
        }
  }
}
