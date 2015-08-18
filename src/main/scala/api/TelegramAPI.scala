package botspot.api

import botspot.api.models._
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scalaj.http.{HttpRequest, Http}


abstract class InteractionCall {def chatId: Int}

case class SendMessageCall (
                             chatId: Int,
                             text: String,
                             disableWebPagePreview: Option[Boolean] = None,
                             replyToMessageId: Option[Int] = None,
                             replyMarkup: Option[Reply] = None) extends InteractionCall

case class ForwardMessageCall(
                               chatId: Int,
                               fromChatId: Int,
                               messageId: Int) extends InteractionCall

case class SendPhotoCall(
                          chatId: Int,
                          photo: String,
                          caption: Option[String] = None,
                          replyToMessageId: Option[Int] = None,
                          replyMarkup: Option[Reply] = None) extends InteractionCall

case class SendAudioCall(
                          chatId: Int,
                          audio: String,
                          duration: Option[Int] = None,
                          replyToMessageId: Option[Int] = None,
                          replyMarkup: Option[Reply] = None) extends InteractionCall

case class SendDocumentCall(
                             chatId: Int,
                             document: String,
                             replyToMessageId: Option[Int] = None,
                             reply: Option[Reply] = None) extends InteractionCall

case class SendStickerCall(
                            chatId: Int,
                            sticker: String,
                            replyToMessageId: Option[Int] = None,
                            reply: Option[Reply] = None) extends InteractionCall

case class SendVideoCall(
                          chatId: Int,
                          video: String,
                          duration: Option[Int] = None,
                          caption: Option[String] = None,
                          replyToMessageId: Option[Int] = None,
                          replyMarkup: Option[Reply] = None) extends InteractionCall

case class SendLocationCall(
                             chatId: Int,
                             latitude: Float,
                             longitude: Float,
                             replyToMessageId: Option[Int] = None,
                             replyMarkup: Option[Reply] = None) extends InteractionCall

case class SendChatActionCall(
                               chatId: Int,
                               action: String) extends InteractionCall

case class GetUserProfilePhotosCall(
                                     userId: Int,
                                     offset: Option[Int] = None,
                                     limit: Option[Int])

object Utils {
  def mapClassToSeq(mes: AnyRef) = {
    mes.getClass.getDeclaredFields.map(f => {
      f.setAccessible(true)
      camelToUnderscores(f.getName) -> (f.get(mes) match {
        case None => f.get(mes)
        case x: Option[Any] => x.get
        case _ => f.get(mes)
      })
    })
  }

  def camelToUnderscores(name: String) = "[A-Z\\d]".r.replaceAllIn(name, { m => "_" + m.group(0).toLowerCase() })
}

class TelegramAPI(botConfig: BotConfig) {
  def getMe(): Bot = {
    val user = telegramJsonRequest("getMe").extract[User]
    new Bot(botConfig.id, botConfig.token, user.firstName, user.lastName, user.username )
  }

  def sendMessage(sendMessageCall: SendMessageCall): Unit = {
    val params = Utils.mapClassToSeq(sendMessageCall)
    telegramJsonRequest("sendMessage", params)
  }

  def sendMessage(
                   chatId: Int,
                   text: String,
                   disableWebPagePreview: Option[Boolean] = None,
                   replyToMessageId: Option[Int] = None,
                   replyMarkup: Option[Reply] = None): Unit = {
    sendMessage(SendMessageCall(chatId, text, disableWebPagePreview, replyToMessageId, replyMarkup))
  }

  def forwardMessage(
                      chatId: Int,
                      fromChatId: Int,
                      messageId: Int): Unit = {
    val params = Utils.mapClassToSeq(ForwardMessageCall(chatId, fromChatId, messageId))
    telegramJsonRequest("forwardMessage", params)
  }

  def sendPhoto(sendPhotoCall: SendPhotoCall): Unit = {
    val params = Utils.mapClassToSeq(sendPhotoCall)
    telegramJsonRequest("sendPhoto", params)
  }

  def sendPhoto(
                 chatId: Int,
                 photo: String,
                 caption: Option[String] = None,
                 replyToMessageId: Option[Int] = None,
                 replyMarkup: Option[Reply] = None): Unit = {
    // TODO file uploading (if photo - InputStream)
    sendPhoto(SendPhotoCall(chatId, photo, caption, replyToMessageId, replyMarkup))
  }

  def sendAudio(
                 chatId: Int,
                 audio: String,
                 duration: Option[Int] = None,
                 replyToMessageId: Option[Int] = None,
                 replyMarkup: Option[Reply] = None): Unit = {
    // TODO file uploading (if audio - InputStream)
    val params = Utils.mapClassToSeq(SendAudioCall(chatId, audio, duration, replyToMessageId, replyMarkup))
    telegramJsonRequest("sendAudio", params)
  }

  def sendDocument(
                    chatId: Int,
                    document: String,
                    replyToMessageId: Option[Int] = None,
                    replyMarkup: Option[Reply] = None): Unit = {
    // TODO file uploading (if document - InputStream)
    val params = Utils.mapClassToSeq(SendDocumentCall(chatId, document, replyToMessageId, replyMarkup))
    telegramJsonRequest("sendDocument", params)
  }

  def sendSticker(
                   chatId: Int,
                   sticker: String,
                   replyToMessageId: Option[Int] = None,
                   replyMarkup: Option[Reply] = None): Unit = {
    // TODO file uploading (if sticker - InputStream)
    val params = Utils.mapClassToSeq(SendStickerCall(chatId, sticker, replyToMessageId, replyMarkup))
    telegramJsonRequest("sendSticker", params)
  }

  def sendVideo(
                 chatId: Int,
                 video: String,
                 duration: Option[Int] = None,
                 caption: Option[String] = None,
                 replyToMessageId: Option[Int] = None,
                 replyMarkup: Option[Reply] = None): Unit = {
    // TODO file uploading (if video - InputStream)
    val params = Utils.mapClassToSeq(SendVideoCall(chatId, video, duration, caption, replyToMessageId, replyMarkup))
    telegramJsonRequest("sendVideo", params)
  }

  def sendLocation(
                    chatId: Int,
                    latitude: Float,
                    longitude: Float,
                    replyToMessageId: Option[Int] = None,
                    replyMarkup: Option[Reply] = None): Unit = {
    val params = Utils.mapClassToSeq(SendLocationCall(chatId, latitude, longitude, replyToMessageId, replyMarkup))
    telegramJsonRequest("sendLocation", params)
  }

  def sendChatAction(
                      chatId: Int,
                      action: String): Unit = {
    // TODO think about convinience of this method
    val params = Utils.mapClassToSeq(SendChatActionCall(chatId, action))
    telegramJsonRequest("sendChatAction", params)
  }


  def getUserProfilePhotos(
                            userId: Int,
                            offset: Option[Int] = None,
                            limit: Option[Int] = None): Unit = {
    // TODO implement
  }


  def getUpdates(
                  offset: Option[Int] = None,
                  limit: Option[Int] = None,
                  timeout: Option[Int] = None): List[Update] = {
    val params = Seq("offset" -> offset, "limit" -> limit, "timeout" -> timeout).
      filter(_._2 != None).map(kv => (kv._1, kv._2.get))
    telegramJsonRequest("getUpdates", params).extract[List[Update]]
  }

  def setWebhook(url: String): Unit = {
    // TODO implement
  }

  private implicit val formats = DefaultFormats

  private val TELEGRAM_API_PATH = "https://api.telegram.org/bot"

  private def telegramJsonRequest(apiMethod: String, params: Seq[(String, Any)] = Seq()) = {
    if (params.isEmpty)
      getResult(apiMethod, query => Http(query))
    else
      getResult(apiMethod, query => Http(query).postForm(params.filter(_._2 != None).map(kv => (kv._1, kv._2.toString))))
  }

  private def getResult(apiMethod: String, queryFunc: String => HttpRequest) =
    parse(queryFunc(queryString(apiMethod)).asString.body).camelizeKeys \ "result"

  private def queryString(apiMethod: String) = s"$TELEGRAM_API_PATH${botConfig.id}:${botConfig.token}/$apiMethod"


}