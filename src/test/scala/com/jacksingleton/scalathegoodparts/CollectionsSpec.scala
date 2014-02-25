package com.jacksingleton.scalathegoodparts

import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import scala.collection.convert.decorateAll._

class CollectionsSpec extends FlatSpec {

  "Java collections" should "be painful" in {
    val stringList = new java.util.ArrayList[String]()

    stringList.add("1")
    stringList.add("2")
    stringList.add("3")

    var total: Int = 0
    for (string <- stringList.asScala) {
      val int = string.toInt

      if (int > 1) {
        total += int
      }
    }

    total should be (5)
  }

  "Scala collections" should "be less painful 1" in {
    val stringList = Seq("1", "2", "3")

    var total: Int = 0
    for (string <- stringList) {
      val int = string.toInt

      if (int > 1) {
        total += int
      }
    }

    total should be (5)
  }

  "Scala collections" should "be less painful 2" in {
    val stringList: Seq[String] = Seq("1", "2", "3")

    val intList: Seq[Int] = stringList map { string =>
      string.toInt
    }

    var total: Int = 0
    for (int <- intList) {
      if (int > 1) {
        total += int
      }
    }

    total should be (5)
  }

  "Scala collections" should "be less painful 3" in {
    val stringList = Seq("1", "2", "3")

    val intList = stringList map { string =>
      string.toInt
    }

    val bigIntList = intList filter { int =>
      int > 1
    }

    var total: Int = 0
    for (int <- bigIntList) {
      total += int
    }

    total should be (5)
  }

  "Scala collections" should "be much nicer" in {
    val stringList = Seq("1", "2", "3")

    val intList = stringList map { string =>
      string.toInt
    }

    val bigIntList = intList filter { int =>
      int > 1
    }

    val total = bigIntList reduce { (a, b) =>
      a + b
    }

    total should be (5)
  }

  "Scala Collections" should "allow concise code" in {
    val stringList = Seq("1", "2", "3")

    val total = stringList
      .map(_.toInt)
      .filter(_ > 1)
      .reduce(_ + _)

    total should be (5)
  }
}
