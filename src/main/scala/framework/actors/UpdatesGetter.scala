package botspot.framework.actors

import java.net.SocketTimeoutException

import akka.actor.{Props, Actor}
import akka.event.Logging
import botspot.api.TelegramAPI
import botspot.api.models.BotConfig

/**
 * Created by petrrezikov on 16.08.15.
 */


class UpdatesGetter(val botConfig: BotConfig) extends Actor {

  val log = Logging(context.system, this)

  private val telegram = new TelegramAPI(botConfig)
  private val pollingTimeout = 2

  def receive = {
    case GetUpdates(updateId) =>  {
      // TODO make async call
      try {
        val updates = telegram.getUpdates(Some(updateId + 1), None, Some(pollingTimeout))
        if (updates.isEmpty) self ! GetUpdates(updateId)
        context.parent ! UpdatesReceived(updates)
      }
      catch {
        case e: Throwable => {
          self ! GetUpdates(updateId)
          log.warning(e.toString)
        }
      }
    }
  }


}

object UpdatesGetter {
  def props(botConfig: BotConfig): Props = Props(new UpdatesGetter(botConfig))

}