package framework

import java.io.File

import akka.actor.{ActorRef, Props, ActorSystem}
import botspot.framework.actors.{StartBot, BotsManager, BotRunner}
import com.typesafe.config.{ConfigFactory, Config}


/**
 * Created by petrrezikov on 19.08.15.
 */
class BotSpotMultiRunner(val configFileName: String, val botInstanceTemplates: Seq[BotTemplate]) {

  def run(): Unit = {
    // TODO: add error checking
    val config = ConfigFactory.parseFile(new File(configFileName))
    val system = ActorSystem("telegramBots")
    val manager = system.actorOf(Props[BotsManager])
    botInstanceTemplates.foreach(runBotActor(manager, config, _))
  }

  def runBotActor(managerActor: ActorRef, config: Config, botTemplate: BotTemplate) =
    managerActor ! StartBot(botTemplate, config.getConfig(botTemplate.name))
}