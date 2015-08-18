package botspot.api.models

import java.util.Date

//abstract class Chat

case class Chat(id: Int, firstName: Option[String], lastName: Option[String],
                username: Option[String], title: Option[String])

case class User(id: Int, firstName: String, lastName: Option[String],
                username: Option[String])

case class GroupChat(id: Int, title: String)

case class PhotoSize(fileId: String, width: Int,
                     height: Int, fileSize: Option[Int])

case class Document(fileId: String, thumb: Option[PhotoSize],
                    fileName: Option[String], mimeType: Option[String],
                    fileSize: Option[Int])

case class Audio(fileId: String, duration: Int,
                 mimeType: Option[String], fileSize: Option[Int])

case class Sticker(fileId: String, width: Int,
                   height: Int, thumb: Option[PhotoSize],
                   fileSize: Option[Int])

case class Video(fileId: String, width: Int,
                 height: Int, duration: Int,
                 thumb: Option[PhotoSize], mimeType: Option[String],
                 fileSize: Option[Int])

case class Contact(phoneNumber: String, firstName: String,
                   lastName: Option[String], userId: Int)

case class Location(longitude: Float, latitude: Float)

case class Message(messageId: Int, from: User, date: Int, chat: Chat,
                   forwardFrom: Option[User], forwardDate: Option[Int],
                   replyToMessage: Option[Message],
                   text: Option[String], audio: Option[Audio],
                   document: Option[Document], photo: Option[List[PhotoSize]],
                   sticker: Option[Sticker], video: Option[Video],
                   caption: Option[String], contact: Option[Contact],
                   location: Option[Location],
                   newChatParticipant: Option[User], leftChatParticipant: Option[User],
                   newChatTitle: Option[String], newChatPhoto: Option[List[PhotoSize]],
                   deleteChatPhoto: Option[Boolean], groupChatCreated: Option[Boolean]) {

  val dateObject = new Date(date * 1000L)

  val forwardDateObject = forwardDate match {
    case None => None
    case Some(_) => Some(new Date(forwardDate.get * 1000L))
  }

}


case class Update(updateId: Int, message: Message)


case class UserProfilePhotos(totalCount: Int, photos: List[PhotoSize])

abstract class Reply(selective: Boolean)

case class ReplyKeyboardMarkup(keyboard: List[List[String]], resizeKeyboard: Boolean,
                               oneTimeKeyboard: Boolean, selective: Boolean) extends Reply(selective)

case class ReplyKeyboardHide(hideKeyboard: Boolean, selective: Boolean) extends Reply(selective)

case class ForceReply(forceReply: Boolean, selective: Boolean) extends Reply(selective)

case class BotConfig(val id: Int, val token: String)

class Bot(val id: Int, val token: String,
          var firstName: String = "", var lastName: Option[String] = None, var username: Option[String] = None)

