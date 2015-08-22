package framework.actors

import akka.actor.{Actor, Props}
import akka.contrib.mailbox.PeekMailboxExtension
import akka.contrib.pattern.ReceivePipeline
import akka.event.Logging
import botspot.api.models.BotConfig
import botspot.api.{TelegramAPI, _}
import com.typesafe.config.Config

/**
 * Created by VR on 17.08.2015.
 * Innopolis University
 * MSIT-SE 2015-2016
 */

class TelegramAPIInteractor(config: Config) extends Actor with ReceivePipeline {

  val log = Logging(context.system, this)

  protected val botConfig = BotConfig(config.getInt("bot.id"), config.getString("bot.token"))

  protected val telegram = new TelegramAPI(botConfig)

  pipelineInner(inner => {case _ => PeekMailboxExtension.ack()})

  override def receive = {
    case SendMessageCall(chatId, text, disableWebPagePreview, replyToMessageId, replyMarkup) =>
      telegram.sendMessage(chatId, text, disableWebPagePreview, replyToMessageId, replyMarkup)
    case ForwardMessageCall(chatId, fromChatId, messageId) =>
      telegram.forwardMessage(chatId, fromChatId, messageId)
    case SendPhotoCall(chatId, photo, caption, replyToMessageId, replyMarkup) =>
      telegram.sendPhoto(chatId, photo, caption, replyToMessageId, replyMarkup)
    case SendAudioCall(chatId, audio, duration, replyToMessageId, replyMarkup) =>
      telegram.sendAudio(chatId, audio, duration, replyToMessageId, replyMarkup)
    case SendDocumentCall(chatId, document, replyToMessageId, replyMarkup) =>
      telegram.sendSticker(chatId, document, replyToMessageId, replyMarkup)
    case SendStickerCall(chatId, sticker, replyToMessageId, replyMarkup) =>
      telegram.sendSticker(chatId, sticker, replyToMessageId, replyMarkup)
    case SendVideoCall(chatId, video, duration, caption, replyToMessageId, replyMarkup) =>
      telegram.sendVideo(chatId, video, duration, caption, replyToMessageId, replyMarkup)
    case SendLocationCall(chatId, latitude, longitude, replyToMessageId, replyMarkup) =>
      telegram.sendLocation(chatId, latitude, longitude, replyToMessageId, replyMarkup)
    case SendChatActionCall(chatId, action) =>
      telegram.sendChatAction(chatId, action)
    case _ =>
      // TODO WAT? Log error
  }
}

object TelegramAPIInteractor {

  def props(config: Config): Props =
    Props(new TelegramAPIInteractor(config))

}
