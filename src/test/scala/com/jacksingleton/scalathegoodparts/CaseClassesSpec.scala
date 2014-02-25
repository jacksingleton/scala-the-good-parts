package com.jacksingleton.scalathegoodparts

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class CaseClassesSpec extends FlatSpec {

  "Mutable entities" should "be familiar to any Java or C# developer" in {
    class Person {
      var name: String = null
    }

    val bob = new Person

    bob.name = "Bob"

    bob.name should be ("Bob")

    bob.name = bob.name + " Senior"

    bob.name should be ("Bob Senior")
  }

  "Mutable entities" should "cause pain when not in a valid state" in {
    class Person {
      var name: String = null
    }

    val bob = new Person

    intercept[NullPointerException] {
      bob.name.startsWith("B")
    }
  }

  "Mutable entities" should "have confusing equality semantics" in {
    class Person {
      var name: String = null
    }

    val bob = new Person
    bob.name = "Bob"

    val alsoBob = new Person
    alsoBob.name = "Bob"

    bob should not be alsoBob
  }

  "Mutable entities" should "take a lot of work to do right" in {
    class Person {
      var name: String = null

      override def equals(obj: Any): Boolean =
        obj match {
          case p: Person => p.name == name
          case _ => false
        }

      override def hashCode() = name.hashCode
    }

    def Person(name: String) = {
      if (name == null) {
        throw new IllegalArgumentException
      } else {
        val p = new Person
        p.name = name
        p
      }
    }

    val bob = Person("Bob")
    val alsoBob = Person("Bob")

    bob should be (alsoBob)
  }

  "Immutable entities" should "be a better solution" in {
    class Person(val name: String) {

      override def equals(obj: Any): Boolean = obj match {
        case p: Person => p.name == name
        case _ => false
      }

      override def hashCode() = name.hashCode
    }

    val bob = new Person("Bob")
    val newBob = new Person(bob.name + " Senior")

    newBob.name should be ("Bob Senior")
  }

  "Immutable entities" should "also be quite cumbersome" in {
    class Person(val name: String,
                 val age: Int,
                 val job: String) {
      override def equals(obj: Any): Boolean = obj match {
        case p: Person =>
          p.name == name &&
          p.age == age &&
          p.job == job
        case _ => false
      }

      override def hashCode() = name.hashCode + age.hashCode + job.hashCode

      def copy(name: String = name,
               age: Int = age,
               job: String = job) =
        new Person(name, age, job)
    }

    val bob = new Person("Bob", 42, "Cryptographer")

    val newBob = bob.copy(name = bob.name + " Senior")

    newBob.name should be ("Bob Senior")
  }

  "Case classes" should "allow for simple value objects" in {
    case class Person(name: String, age: Int, job: String)

    val bob = Person("Bob", 42, "Cryptographer")

    val newBob = bob.copy(name = bob.name + " Senior")

    newBob.name should be ("Bob Senior")
  }

  "Case classes" should "allow pattern matching" in {
    case class Person(name: String, age: Int, job: String)

    val bob = Person("Bob", 42, "Cryptographer")

    bob match {
      case Person(name, age, job) => age should be (42)
    }
  }

  "Pattern matching" should "be quite powerful" in {
    case class Name(first: String, last: String)
    case class Person(name: Name, age: Int, job: String)

    val bob = Person(
      name = Name("Bob", "Smith"),
      age = 42,
      job = "Cryptographer"
    )

    bob match {
      case Person(Name(first, "Smith"), _, _) =>
        first should be ("Bob")
      case Person(Name(first, "Jones"), _, _) =>
        fail("Last name should not be Jones")
    }
  }
}
