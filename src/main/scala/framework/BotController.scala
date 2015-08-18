package botspot.framework

import botspot.api.TelegramAPI
import botspot.api.models.{BotConfig, Message}
import com.typesafe.config._


/**
 * Created by petrrezikov on 15.08.15.
 */
abstract class BotController(val config: Config) {

  protected val botConfig = BotConfig(config.getInt("bot.id"), config.getString("bot.token"))

  protected val telegram = new TelegramAPI(botConfig)


  protected def sendMessageToId(receiverId: Int, text: String) = {
    telegram.sendMessage(receiverId, text)
  }

  protected def sendStickerToId(receiverId: Int, sticker: String)=
    telegram.sendSticker(receiverId, sticker)


  protected def sendAudioToId(receiverId: Int, audio: String) =
    telegram.sendAudio(receiverId, audio)


  protected def sendPhotoToId(receiverId: Int, photo: String) =
    telegram.sendPhoto(receiverId, photo)

  def receive(message: Message)

}
