package botspot.framework.actors

import java.net.SocketTimeoutException

import akka.actor.{Props, Actor}
import botspot.api.TelegramAPI
import botspot.api.models.BotConfig

/**
 * Created by petrrezikov on 16.08.15.
 */


class UpdatesGetter(val botConfig: BotConfig) extends Actor {

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
        case e: SocketTimeoutException => self ! GetUpdates(updateId)
      }
    }
  }


}

object UpdatesGetter {
  def props(botConfig: BotConfig): Props = Props(new UpdatesGetter(botConfig))

}