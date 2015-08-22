package botspot.framework.actors
import akka.actor.{Props, Actor}
import akka.event.Logging
import botspot.api.models.BotConfig

/**
 * Created by petrrezikov on 16.08.15.
 */

class MessageReceiver(val botConfig: BotConfig) extends Actor {

  val log = Logging(context.system, this)
  val updatesGetter = context.actorOf(UpdatesGetter.props(botConfig), "updatesGetter")
  val updateIdManager = context.actorOf(UpdateIdManager.props(botConfig.id), "updateIdManager")

  updateIdManager ! GetUpdateId

  def receive = {
    case UpdateId(updateId) => updatesGetter ! GetUpdates(updateId)
    case UpdatesReceived(updates) if updates.length != 0 => {
      context.parent ! MessagesReceived(updates.map(_.message))
      updateIdManager ! SetUpdateId(updates.map(_.updateId).max)
    }
  }


}

object MessageReceiver {
  def props(botConfig: BotConfig): Props = Props(new MessageReceiver(botConfig))
}
