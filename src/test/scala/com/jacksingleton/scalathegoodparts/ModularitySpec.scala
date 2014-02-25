package com.jacksingleton.scalathegoodparts

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class ModularitySpec extends FlatSpec {

  "The cake pattern" should "be a bit of a mouthful" in {
    trait NumberComponent {
      trait Number {
        def int: Int
      }

      protected def number: Number
    }

    trait OneComponent extends NumberComponent {
      class One extends Number {
        def int: Int = 1
      }

      protected def number: Number = new One
    }

    class Application { this: NumberComponent =>

      def run() {
        number.int should be (1)
      }
    }

    (new Application with OneComponent).run()
  }

  "Traits for dependencies" should "be avoided" in {
    trait Number {
      protected def int: Int
    }

    trait One extends Number {
      protected def int: Int = 1
    }

    class Application extends One {

      def run() {
        int should be (1)
      }
    }

    (new Application).run()
  }

  "Implicits for modularity" should "be unclear and untestable" in {
    implicit class OneInt(one: Number) {
      def int: Int = 1
    }

    class Number

    class Application(one: Number) {

      def run() {
        one.int should be (1)
      }
    }

    new Application(new Number).run()
  }

  "Constructor injection" should "be a lot more understandable" in {
    trait Number {
      def int: Int
    }

    class One extends Number {
      def int: Int = 1
    }

    class Application(number: Number) {

      def run() {
        number.int should be (1)
      }
    }

    new Application(new One).run()
  }

  "Super classes" should "not have tons of behavior" in {
    trait Number {
      def int: Int
      def string: String = s"Number $int"
    }

    class One extends Number {
      def int: Int = 1
    }

    (new One).string should be ("Number 1")
  }

  "Traits as mixins" should "help separate behaviour from entities" in {
    trait Number {
      def int: Int
    }

    trait AsString {
      protected def int: Int
      def string: String = s"Number $int"
    }

    class One extends Number with AsString {
      def int: Int = 1
    }

    (new One).string should be ("Number 1")
  }

  "Trait mixin at instantiation time" should "further decouple behaviour from entities" in {
    trait Number {
      def int: Int
    }

    trait AsString {
      protected def int: Int
      def string: String = s"Number $int"
    }

    class One extends Number {
      def int: Int = 1
    }

    val instance = new One with AsString

    instance.string should be ("Number 1")
  }
}
