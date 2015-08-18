package botspot.framework

import akka.actor.ActorRef
import botspot.api.{SendMessageCall, TelegramAPI}
import botspot.api.models.{BotConfig, Message}
import botspot.framework.actors.TelegramApiCall
import com.typesafe.config._


/**
 * Created by petrrezikov on 15.08.15.
 */
class BotController(val config: Config) {

  protected val botConfig = BotConfig(config.getInt("bot.id"), config.getString("bot.token"))

  protected val telegram = new TelegramAPI(botConfig)

  protected var _telegramApiInteractor: ActorRef = null

  def telegramApiInteractor = _telegramApiInteractor

  def telegramApiInteractor_= (value: ActorRef): Unit = _telegramApiInteractor = value

  protected def sendMessageToId(receiverId: Int, text: String) = {
    telegramApiInteractor ! TelegramApiCall(SendMessageCall(receiverId, text))
  }

  protected def sendStickerToId(receiverId: Int, sticker: String)=
    telegram.sendSticker(receiverId, sticker)


  protected def sendAudioToId(receiverId: Int, audio: String) =
    telegram.sendAudio(receiverId, audio)


  protected def sendPhotoToId(receiverId: Int, photo: String) =
    telegram.sendPhoto(receiverId, photo)

  protected def sendVideoToId(receiverId: Int, video: String) =
    telegram.sendVideo(receiverId, video)

  def receive(message: Message): Unit = {}
}
