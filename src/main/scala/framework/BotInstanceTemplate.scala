package framework

import botspot.framework.BotController
import com.typesafe.config.Config

/**
 * Created by petrrezikov on 19.08.15.
 */

case class BotTemplate(val name: String,
                       val botLogicFactoryFunc: Config => BotController)
