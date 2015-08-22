package botspot.framework.actors

import java.io.File

import akka.actor.{Props, Actor}
import akka.event.Logging

import scala.io.Source

/**
 * Created by petrrezikov on 16.08.15.
 */
class UpdateIdManager(val botId: Int) extends Actor {


  val log = Logging(context.system, this)

  var updateId = readUpdateId()


  def receive = {
    case GetUpdateId => sendUpdateId()
    case SetUpdateId(_updateId) => {
      updateId = _updateId
      sendUpdateId()
      syncUpdateId()
    }
  }

  private def sendUpdateId() = context.parent ! UpdateId(updateId)

  // TODO: make async writing
  private def syncUpdateId() = {
    val p = new java.io.PrintWriter(new File(updateIdFileName))
    try {
      p.write(updateId.toString + "\n")
    }
    finally p.close()
  }


  private def readUpdateId(): Int = {
    if (!new java.io.File(updateIdFileName).exists)
      0
    else // TODO: make async reading
      Source.
        fromFile(updateIdFileName).getLines.toList.head.toInt
  }

  private def updateIdFileName: String = s"update_id_$botId.txt"

}



object UpdateIdManager {
  def props(botId: Int): Props = Props(new UpdateIdManager(botId))

}


