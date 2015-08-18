package botspot.framework

import botspot.api.models.{Update, Message}

/**
 * Created by petrrezikov on 15.08.15.
 */
package object actors {

  case class MessagesReceived(messages: List[Message])
  case object GetUpdateId
  case class UpdatesReceived(update: List[Update])
  case class UpdateId(updateId: Int)
  case class GetUpdates(updateId: Int)
  case class SetUpdateId(updateId: Int)

}
