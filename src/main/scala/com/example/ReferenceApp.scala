package com.example

import akka.actor.{ActorDSL, ActorSystem, Props}
import akka.routing.FromConfig

import scala.concurrent.TimeoutException

object ReferenceApp extends App {
  override def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem.apply("ReferenceApp")
    // router1配下に$a、$b、$cのActorが生成される
    val router1 = system.actorOf(FromConfig.props(Props[MessagePrintActor]),
      "router1")

    val actors = system.actorSelection("/user/router1/*")
    val rootInbox = ActorDSL.inbox()
    actors.tell("Path1", rootInbox.getRef())
    actors.tell("Path2", rootInbox.getRef())

    // 非同期処理のため、以後のreceiveがPath2の到着前に実行されるのを防止するために待ちを入れる
    Thread.sleep(1000)

    val received1 = rootInbox.receive()
    println("received1:" + received1)
    val received2 = rootInbox.receive()
    println("received2:" + received2)
    val received3 = rootInbox.receive()
    println("received3:" + received3)
    try {
      val received4 = rootInbox.receive()
      println("received4:" + received4)
    }
    catch {
      case ex:TimeoutException => { println("Exception Occured." + ex.getMessage)}
    }

    actors.tell("Path3", rootInbox.getRef())
    val received5 = rootInbox.receive()
    println("received5:" + received5)

    router1 ! "Test1"

    Thread.sleep(1000)
    system.shutdown()
  }
}
