package com.jacksingleton.scalathegoodparts

import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import scala.Some

class ForSpec extends FlatSpec {

  "Lots of maps" should "create spaceships" in {
    def lookup1(i: Int) = if (i == 0) Some("result") else None
    def lookup2(s: String) = if (s == "result") Some(42) else None
    def lookup3(i: Int) = if (i < 50) Some(i * i) else None
  
    // We call this a spaceship
    val maybeResult: Option[String] =
      lookup1(0) flatMap { string =>
        lookup2(string) flatMap { int =>
          lookup3(int) map { squared =>
            squared.toString
          }
        }
      }
  
    val output: String = maybeResult getOrElse "nada!"
  
    output should be ("1764")
  }

  "For comprehensions" should "destroy the spaceship" in {
    def lookup1(i: Int) = if (i == 0) Some("result") else None
    def lookup2(s: String) = if (s == "result") Some(42) else None
    def lookup3(i: Int) = if (i < 50) Some(i * i) else None

    val maybeResult: Option[String] = for {
      string <- lookup1(0)
      int <- lookup2(string)
      squared <- lookup3(int)
    } yield squared.toString

    val output: String = maybeResult getOrElse "nada!"

    output should be ("1764")
  }

  "Tips" should "be helpful" in {
    (for (i <- Seq(1, 2)) yield i * i) should be (Seq(1, 4))

    // Avoid
    (for (r <- Some("result")) r) should be ()
  }
}
