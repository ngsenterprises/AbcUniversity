package com.abc.university.shapes

sealed trait GradeAssociate
case class Course(id: String, name: String, dept: String, professor: String) extends GradeAssociate
case class Student(id: String, name: String, year: String) extends GradeAssociate
case class Enrollment(studentId: String, courseId: String, grade: Double) extends GradeAssociate

object Shapes {
  val recordSeparator = "\n"
  val fieldSeparator = "\\t"

  sealed trait FieldShape {
    def fields: Seq[String]
    override def toString = fields.mkString(" ")
  }
  case object CourseShape extends FieldShape { val fields = Seq("COURSE_ID", "COURSE_NAME", "DEPARTMENT", "PROFESSOR") }
  case object StudentShape extends FieldShape { val fields = Seq("STUDENT_ID", "STUDENT_NAME", "YEAR") }
  case object EnrollmentShape extends FieldShape { val fields = Seq("STUDENT_ID", "COURSE_ID", "GRADE") }
  case object Freshman extends FieldShape { val fields = Seq("Freshman") }
  case object Sophomore extends FieldShape { val fields = Seq("Sophomore") }
  case object Junior extends FieldShape { val fields = Seq("Junior") }
  case object Senior extends FieldShape { val fields = Seq("Senior") }



}
