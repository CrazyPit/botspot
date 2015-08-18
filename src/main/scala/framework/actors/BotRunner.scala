package botspot.framework.actors

import akka.actor.{Props, Actor}
import botspot.api.models.BotConfig
import botspot.framework.BotController
import com.typesafe.config._

/**
 * Created by petrrezikov on 15.08.15.
 */


class BotRunner(botConfig: BotConfig, config: Config, val factoryFunc: Config => BotController) extends Actor {

  val messageReceiver =
    context.actorOf(MessageReceiver.props(botConfig), "messagesReceiver")
  val logicManager  = context.actorOf(BotLogicManager.props(config, factoryFunc), "botLogicManager")

  def receive = {
    case MessagesReceived(messages) => {
      messages.foreach(logicManager ! _)
    }
  }

}


object BotRunner {
  def props(botConfig: BotConfig, config: Config, factoryFunc: Config => BotController): Props = {
    Props(new BotRunner(botConfig, config, factoryFunc))
  }
}
