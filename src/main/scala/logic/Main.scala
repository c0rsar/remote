package logic
import akka.actor.{ Actor, ActorSystem, Props }
import akka.event.Logging

/**
  * Created by anatoliyzizda on 8/11/19.
  */
object Main {

  val system = ActorSystem("app")
  val processor = system.actorOf(Props[CommandProcessor], name = "processor")
  val comm = system.actorOf(Props[Comm], name = "comm")
  val display = system.actorOf(Props[Display], name = "display")

  def main(args: Array[String]): Unit = {
    println("ok")

    init()

    readInput()

    println("end execution")

  }


  def readInput(): Unit = {
    val input = scala.io.StdIn.readLine()
    input match {
      case "end" => // exit
      case c: String => {
        processor ! InputMsg(c)
        readInput()
      }
    }
  }



  def init() {
    println("init application")
  }
  def exit(): Unit = {

    println("exit application")
  }

}


class Display extends Actor {

  def receive = {

    case msg: DisplayMsg => {
      println(msg.msg)
    }

  }

}

class Comm extends Actor {

  def receive = {

    case msg:SerialInMsg => {
      Main.processor ! SerialOutMsg("FA00007000000")
    }
    case _ =>

  }

}


class CommandProcessor extends Actor {

  def receive = {
   // case msg:InputMessage => Main.display ! DisplayMsg(s"got message ${msg.msg}")

    case msg:InputMsg => {
      // inputToCommand()
      Main.comm ! SerialInMsg(msg.msg)
    }
    case msg: SerialOutMsg => {
      Main.display ! DisplayMsg(msg.msg)
    }

    case _ => println(" A 2 received a message")
  }

}


class A1 extends Actor {

  def receive = {
    case _ => println("a1 received msg")
  }

}

case class InputMsg(msg:String)
case class SerialInMsg(msg:String)
case class SerialOutMsg(msg:String)

case class DisplayMsg(msg:String)