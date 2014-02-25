package com.jacksingleton.scalathegoodparts

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class ErrorHandlingSpec extends FlatSpec {

  "Error handling with exceptions" should "be ugly" in {
    class LookupException extends Exception

    def lookup(i: Int): String = throw new LookupException

    val result = try {
      lookup(1)
    } catch {
      case e: LookupException => "nada!"
    }

    result should be ("nada!")
  }

  "Error handling with Either" should "be more explicit" in {
    case class LookupError(msg: String)

    def lookup(i: Int): Either[LookupError, String] =
      Left(LookupError("nada!"))

    val result: String = lookup(1) match {
      case Right(s) => s
      case Left(LookupError(msg)) => msg
    }

    result should be ("nada!")
  }

  "More complex error handling with Either" should "be even more explicit" in {
    sealed trait LookupError
    case class NotFound() extends LookupError
    case class DatabaseUnavailable() extends LookupError

    def lookup: Either[LookupError, String] =
      if (false) Right("result") else Left(NotFound())

    val result = lookup match {
      case Right(string) => string
      case Left(NotFound()) => "not found"
    }

    result should be ("not found")

    // [warn]
    // ErrorHandlingSpec.scala:64: match may not be exhaustive.
    // It would fail on the following input: Left(DatabaseUnavailable())
    //   val result = lookup match {
  }

}
