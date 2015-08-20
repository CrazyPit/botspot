package framework

import java.io.File

import akka.actor.{ActorRef, Props, ActorSystem}
import botspot.framework.actors.{StartBot, BotsManager, BotRunner}
import com.typesafe.config.{ConfigFactory, Config}
import scala.io.Source

/**
 * Created by petrrezikov on 19.08.15.
 */
class BotSpotMultiRunner(val configFileNames: Array[String], val botInstanceTemplates: Seq[BotTemplate]) {

  def run(): Unit = {
    // TODO: add error checking
    val config =
      ConfigFactory.parseString(
        configFileNames.flatMap(Source.fromFile(_).getLines()).mkString("\n"))
    val system = ActorSystem("telegramBots", ConfigFactory.parseString( """
                                          telegram-http-dispatcher {
                                            mailbox-type = "akka.contrib.mailbox.PeekMailboxType"
                                            max-retries = 5
                                          }"""))
    val manager = system.actorOf(Props[BotsManager])
    botInstanceTemplates.foreach(runBotActor(manager, config, _))
  }

  def runBotActor(managerActor: ActorRef, config: Config, botTemplate: BotTemplate) =
    managerActor ! StartBot(botTemplate, config.getConfig(botTemplate.name))
}
