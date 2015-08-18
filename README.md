# BotSpot 

BotSpot is a Scala framework for Telegram bots based on Akka.

## Usage

### Code

```scala
package innobots.anonbot
import com.typesafe.config._
import botspot.api.models.Message
import botspot.framework.{BotSpotRunner, BotController}

class AnonymousChatBot(override val config: Config) extends BotController(config) {

  private def groupId: Int = config.getInt("groupId")

  override def receive(message: Message): Unit = (message.sticker, message.audio, message.photo, message.text) match {
    case (Some(sticker), _, _, _) => sendStickerToId(groupId, sticker.fileId)
    case (_, Some(audio), _, _) => sendAudioToId(groupId, audio.fileId)
    case (_, _, Some(photo), _) => sendPhotoToId(groupId, photo.last.fileId)
    case (_, _, _, Some(text)) => message.text match {
      case Some("/start") =>
      case Some(text) => sendMessageToId(groupId, text)
    }
    case _ =>
  }

}


object AnonymousChatBot extends App {
  new BotSpotRunner("anonchat", args(0), (config: Config) => new AnonymousChatBot(config)).run()
}

```

### Config

```config
anonchat.bot.token = "SECRET_TOKEN"
anonchat.bot.id = 12345678
anonchat.groupId = -123456
```


### Run

```bash
java -jar anonbot.jar anonbot.conf
```
