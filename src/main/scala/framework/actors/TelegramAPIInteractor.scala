package framework.actors

import akka.actor.{Actor, Props}
import akka.contrib.mailbox.PeekMailboxExtension
import botspot.api.models.BotConfig
import botspot.api.{SendMessageCall, SendPhotoCall, TelegramAPI}
import botspot.framework.actors.TelegramApiCall
import com.typesafe.config.Config

/**
 * Created by VR on 17.08.2015.
 * Innopolis University
 * MSIT-SE 2015-2016
 */

class TelegramAPIInteractor(config: Config) extends Actor {

  protected val botConfig = BotConfig(config.getInt("bot.id"), config.getString("bot.token"))

  protected val telegram = new TelegramAPI(botConfig)

  override def receive = {
    case TelegramApiCall(_interactionCall: SendMessageCall) => {
      telegram.sendMessage(_interactionCall.asInstanceOf[SendMessageCall])
      PeekMailboxExtension.ack()
    }
    case TelegramApiCall(_interactionCall: SendPhotoCall) => {
      telegram.sendPhoto(_interactionCall.asInstanceOf[SendPhotoCall])
      PeekMailboxExtension.ack()
    }

  }
}

object TelegramAPIInteractor {

  def props(config: Config): Props =
    Props(new TelegramAPIInteractor(config))

}
