package com.example

import akka.actor.Actor

class MessagePrintActor extends Actor {

  def receive = {
    case msg: String => {
      val message = self.path + ": Received String " + msg
      println(message)
    }
  }
}
