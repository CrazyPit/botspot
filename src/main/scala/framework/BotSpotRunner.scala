package botspot.framework

import java.io.File

import akka.actor.ActorSystem
import botspot.framework.actors.BotRunner
import com.typesafe.config.{Config, ConfigFactory}

//import botspot.AnonymousChatBotAsync

import botspot.api.models.BotConfig


/**
 * Created by petrrezikov on 15.08.15.
 */
class BotSpotRunner(val botName: String, configFileName: String,
                    val botLogicFactoryFunc: Config => BotController) {

  def run(): Unit = {
    // TODO: add error checking
    val config = ConfigFactory.parseFile(new File(configFileName))
    val system = ActorSystem(botName)
    system.actorOf(BotRunner.props(config.getConfig(botName), botLogicFactoryFunc), "controller")
  }
}
