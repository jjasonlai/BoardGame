package main.network;

import Messaging.Message;
import Messaging.MessageData;

public class ServerMessage extends Message {
   public ServerMessage(MessageData messageData, ServerMessageType serverMessageType) {
      super(messageData, serverMessageType);
   }
}
