package framework

import akka.actor.{ActorRef, Props, ActorSystem}
import botspot.framework.actors.{StartBot, BotsManager, BotRunner}
import com.typesafe.config.{ConfigFactory, Config}
import scala.io.Source
import java.io.File

/**
 * Created by petrrezikov on 19.08.15.
 */
class BotSpotMultiRunner(val configFileNames: Array[String], val botInstanceTemplates: Seq[BotTemplate]) {

  val defaultAkkaConfig = """
    telegram-http-dispatcher {
      mailbox-type = "akka.contrib.mailbox.PeekMailboxType"
      max-retries = 5
    }"""

  def run(): Unit = {
    // TODO: add error checking

    val config = loadConfigs
    val system = ActorSystem("telegramBots", config)
    val manager = system.actorOf(Props[BotsManager])
    botInstanceTemplates.foreach(runBotActor(manager, config, _))
  }

  private def runBotActor(managerActor: ActorRef, config: Config, botTemplate: BotTemplate) =
    managerActor ! StartBot(botTemplate, config.getConfig(botTemplate.name))

  private def loadConfigs =
    configFileNames.toSeq.
      foldLeft(ConfigFactory.load())((config, fileName) => {
      ConfigFactory.parseFile(new File(fileName)).withFallback(config)
    }).withFallback(ConfigFactory.parseString(defaultAkkaConfig))

}
