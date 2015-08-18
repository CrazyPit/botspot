package botspot.framework.actors

import akka.actor.{Props, Actor}
import botspot.api.models.Message
import botspot.framework.BotController
import com.typesafe.config._


/**
 * Created by petrrezikov on 16.08.15.
 */
class BotLogicManager(config: Config, factory: Config => BotController) extends Actor {

  var logic = factory(config)


  def receive = {
    case message: Message => logic.receive(message)
  }


}

object BotLogicManager {

  def props(config: Config, factoryFunc: Config => BotController): Props =
    Props(new BotLogicManager(config, factoryFunc))

}
