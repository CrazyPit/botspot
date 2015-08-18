package botspot.framework.actors

import akka.actor.{ActorSystem, Actor, Props}
import botspot.api.models.Message
import botspot.framework.BotController
import com.typesafe.config._
import framework.actors.TelegramAPIInteractor


/**
 * Created by petrrezikov on 16.08.15.
 */
class BotLogicManager(config: Config, factory: Config => BotController) extends Actor {

  val
  peekSystem = ActorSystem("PeekSystem", ConfigFactory.parseString( """
    peek-dispatcher {
      mailbox-type = "akka.contrib.mailbox.PeekMailboxType"
      max-retries = 5
    }
    """))

  val telegramApiInteractor = peekSystem.actorOf(TelegramAPIInteractor.props(config).withDispatcher("peek-dispatcher"))

  var logic = factory(config)

  override def preStart = {
    logic.telegramApiInteractor = telegramApiInteractor
  }

  def receive = {
    case message: Message => logic.receive(message)
  }
}

object BotLogicManager {

  def props(config: Config, factoryFunc: Config => BotController): Props =
    Props(new BotLogicManager(config, factoryFunc))

}
