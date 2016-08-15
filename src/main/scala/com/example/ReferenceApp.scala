package com.example

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig

object ReferenceApp extends App {
  override def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem.apply("ReferenceApp")
    // router1配下に$a,$b,$cのActorが生成される
    val router1 = system.actorOf(FromConfig.props(Props[MessagePrintActor]), "router1")
    // val router1 = system.actorOf(Props[MessagePrintActor], "router1")

    // フルパス
    // val actor1 = system.actorSelection("akka://ReferenceApp/user/router1/$b")
    // 相対パス
    // val actor1 = system.actorSelection("/user/router1/$b")
    val actors = system.actorSelection("/user/router1/*")

    // actor1 ! "Message1"
    actors ! "Message1"

    router1 ! "Message2"
    router1 ! "Message3"

    Thread.sleep(1000)
    system.shutdown()
  }
}
