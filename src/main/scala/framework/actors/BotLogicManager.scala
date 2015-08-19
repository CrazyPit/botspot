package botspot.framework.actors

import java.net.SocketTimeoutException

import akka.actor.SupervisorStrategy.{Restart, Escalate}
import akka.actor._
import botspot.api.models.Message
import botspot.framework.BotController
import com.typesafe.config._
import framework.actors.TelegramAPIInteractor
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by petrrezikov on 16.08.15.
 */
class BotLogicManager(config: Config, factory: Config => BotController) extends Actor {

  var telegramApiInteractor: Option[ActorRef] = None

  var logic = factory(config)

  private def initInteractor(): Unit = {
    telegramApiInteractor =
      Some(context.watch(context.actorOf(TelegramAPIInteractor.props(config).withDispatcher("telegram-http-dispatcher"))))
    logic.telegramApiInteractor = telegramApiInteractor
  }

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 10 seconds) {
    case e: SocketTimeoutException =>
      println(e)
      Restart
    case e: Exception =>
      println(e)
      Escalate
  }

  override def preStart = {
    initInteractor()
  }

  def receive = {
    case message: Message => logic.receive(message)
    case Terminated(actorRef) if Some(actorRef) == telegramApiInteractor =>
      telegramApiInteractor = None
      logic.telegramApiInteractor = None
      context.system.scheduler.scheduleOnce(5 seconds, self, "Restart")
    case "Restart" => initInteractor()
  }
}

object BotLogicManager {

  def props(config: Config, factoryFunc: Config => BotController): Props =
    Props(new BotLogicManager(config, factoryFunc))

}
