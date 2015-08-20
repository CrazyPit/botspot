package botspot.framework

import akka.actor.ActorRef
import botspot.api.TelegramAPI
import botspot.api._
import botspot.api.models.{Reply, BotConfig, Message}
import com.typesafe.config._


/**
 * Created by petrrezikov on 15.08.15.
 */
class BotController(val config: Config) {

  protected val botConfig = BotConfig(config.getInt("bot.id"), config.getString("bot.token"))

  protected val telegram = new TelegramAPI(botConfig)

  protected var _telegramApiInteractor: Option[ActorRef] = None

  def telegramApiInteractor = _telegramApiInteractor

  def telegramApiInteractor_=(value: Option[ActorRef]): Unit = _telegramApiInteractor = value

  protected def sendMessageToId(receiverId: Int, text: String, replyMarkup: Option[Reply] = None) =
    telegram.sendMessage(receiverId, text, replyMarkup = replyMarkup)

  protected def sendStickerToId(receiverId: Int, sticker: String) =
    telegram.sendSticker(receiverId, sticker)

  protected def sendAudioToId(receiverId: Int, audio: String) =
    telegram.sendAudio(receiverId, audio)

  protected def sendPhotoToId(receiverId: Int, photo: String) =
    telegram.sendPhoto(receiverId, photo)

  protected def sendVideoToId(receiverId: Int, video: String) =
    telegram.sendVideo(receiverId, video)


  // Asynchronous version of Telegram API calls, that sends message to special actor

  protected def sendMessageToIdAsync(receiverId: Int, text: String) = {
    telegramApiInteractor.get ! SendMessageCall(receiverId, text)
  }

  protected def sendStickerToIdAsync(receiverId: Int, sticker: String) =
    telegramApiInteractor.get ! SendStickerCall(receiverId, sticker)

  protected def sendAudioToIdAsync(receiverId: Int, audio: String) =
    telegramApiInteractor.get ! SendAudioCall(receiverId, audio)

  protected def sendPhotoToIdAsync(receiverId: Int, photo: String) =
    telegramApiInteractor.get ! SendPhotoCall(receiverId, photo)

  protected def sendVideoToIdAsync(receiverId: Int, video: String) =
    telegramApiInteractor.get ! SendVideoCall(receiverId, video)

  def receive(message: Message): Unit = {}
}
