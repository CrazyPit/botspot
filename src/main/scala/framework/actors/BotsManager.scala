package botspot.framework.actors

import akka.actor.Actor
import akka.event.Logging
import com.typesafe.config.Config
import framework.BotTemplate
/**
 * Created by petrrezikov on 15.08.15.
 */


case class StartBot(botTemplate: BotTemplate, config: Config)
case class RestartBot(name: String)
case class StopBot(name: String)
case class TerminateBot(name: String)
case object ShutdownSystem

class BotsManager extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case StartBot(botTemplate, config) =>
      context.actorOf(BotRunner.props(config, botTemplate.botLogicFactoryFunc),  s"${botTemplate.name}_controller")
    case RestartBot(botName) => log.info("received unknown message")
    case StopBot(botName) =>
    case TerminateBot(botName) =>
    case ShutdownSystem =>
  }

}
