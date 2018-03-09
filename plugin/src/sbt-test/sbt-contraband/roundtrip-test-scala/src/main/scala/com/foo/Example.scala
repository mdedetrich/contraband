package com.foo

import sjsonnew.JsonFormat
import sjsonnew.support.scalajson.unsafe.{ Converter, CompactPrinter }
import com.example._

object Example extends App {
  import codec.CustomJsonProtocol._
  import codec.FruitJsonProtocol._
  val g0: Greeting = SimpleGreeting("Hello")
  val g1: Greeting = SimpleGreeting("Hello", None)
  val g21: Greeting = GreetingWithAttachments("Hello", Vector.empty)
  val g3: Greeting = GreetingWithOption("Hello", Option("foo"))
  val gl1: GreetingList = GreetingList(Vector(g0))
  val gl2: GreetingList = GreetingList(Vector(g0))

  val json = CompactPrinter(Converter.toJson(g0).get)
  println(json)
  assert(json == """{"$type":"SimpleGreeting","message":"Hello"}""")
  println(Converter.fromJson[Greeting](Converter.toJson(g0).get).get)

  assert(Converter.fromJson[Greeting](Converter.toJson(g0).get).get == g0)
  assert(Converter.fromJson[Greeting](Converter.toJson(g1).get).get == g1)
  assert(Converter.fromJson[Greeting](Converter.toJson(g21).get).get == g21)


  val json3 = CompactPrinter(Converter.toJson(g3).get)
  println(json3)
  assert(json3 == """{"$type":"GreetingWithOption","message":"Hello","opt":"foo"}""")
  println(Converter.fromJson[Greeting](Converter.toJson(g3).get).get)
  assert(Converter.fromJson[Greeting](Converter.toJson(g3).get).get == g3)

  val f0: Fruit = Orange()
  val json4 = CompactPrinter(Converter.toJson(f0).get)
  println(json4)
  assert(Converter.fromJson[Fruit](Converter.toJson(f0).get).get == f0)

  assert(gl1 == gl2)
  assert(gl1.## == gl2.##)
}
