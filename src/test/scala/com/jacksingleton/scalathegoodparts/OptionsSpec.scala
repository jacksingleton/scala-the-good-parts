package com.jacksingleton.scalathegoodparts

import org.scalatest.FlatSpec

import org.scalatest.Matchers._
import scala.util.Try

class OptionsSpec extends FlatSpec {

  "Using null" should "be ugly" in {
    def lookup(i: Int) = if (i == 0) "result" else null

    val maybeResult: String = lookup(0)

    val output: String =
      if (maybeResult != null) maybeResult else "nada!"

    output should be ("result")
  }

  "Using Option" should "be better" in {
    def lookup(i: Int) =
      if (i == 0) Some("result") else None

    val maybeResult: Option[String] = lookup(0)

    val output: String = maybeResult getOrElse "nada!"

    output should be ("result")
  }

  "Using null in more complex scenario" should "be uglier" in {
    def lookup1(i: Int) = if (i == 0) "result" else null
    def lookup2(s: String) = if (s == "result") 42 else null
    
    val maybeString: String = lookup1(0)

    val output: String = if (maybeString == null) {
      null
    } else {
      val maybeInt = lookup2(maybeString)
      if (maybeInt == null) {
        null
      } else {
        maybeInt.toString
      }
    }
    
    output should be ("42")
  }

  "Using Option in more complex scenario" should "be in need of a flatMap" in {
    def lookup1(i: Int) = if (i == 0) Some("result") else None
    def lookup2(s: String) = if (s == "result") Some(42) else None

    val maybeResult: Option[Option[String]] =
      lookup1(0) map { string =>
        lookup2(string) map { int =>
          int.toString
        }
      }

    val flatMaybeResult: Option[String] = maybeResult match {
      case Some(Some(x)) => Some(x)
      case _ => None
    }

    val output: String = flatMaybeResult getOrElse "nada!"

    output should be ("42")
  }

  "Using Option in more complex scenario" should "be much less ugly" in {
    def lookup1(i: Int) = if (i == 0) Some("result") else None
    def lookup2(s: String) = if (s == "result") Some(42) else None

    val maybeResult: Option[String] =
      lookup1(0) flatMap { string =>
        lookup2(string) map { int =>
          int.toString
        }
      }

    val output: String = maybeResult getOrElse "nada!"

    output should be ("42")
  }

  "Tips" should "be helpful" in {
    Option(null) should be (None)

    // Avoid:
    Some("thing").isDefined should be (true)
    Some("thing").get should be ("thing")
  }
}
