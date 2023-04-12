package Messaging;

import com.google.gson.*;

/**
 * This needs simplification
 * @author Jason
 */
public class Message {
    // enumerations
    public enum ClientMessageType { EndTurn, ShowCard }
    public enum ServerMessageType { WaitingForPlayers, InitializeGame, NewTurn, ShowCard, PromptDisprovingCards, ReportGameResults }

    // private fields
    private ClientMessageType clientMessageType;
    private ServerMessageType serverMessageType; 
    private transient Gson gson = new Gson();
    
    // data fields
    protected MessageData data;
    
    // constructors
    /**
     * 
     * @param messageType
     * @param messageData
     * @param serverMessageType
     */
    public Message(MessageData messageData, ServerMessageType serverMessageType )
    {
       this.data = messageData;
       this.serverMessageType = serverMessageType;
    }
    public Message(MessageData messageData, ClientMessageType clientMessageType )
    {
       this.data = messageData;
       this.clientMessageType = clientMessageType;
    }
    
    // public helper methods
    
    /**
     * Custom nested JSON deserializer. Very complicated, I hate Java.
     */
    public static Message Deserialize(String json) {  
       try {
          Gson gson = new Gson();
          JsonElement element = gson.fromJson(json, JsonElement.class);
          JsonObject obj = element.getAsJsonObject();
          ClientMessageType cmt = null;
          ServerMessageType smt = null;
          try{
             cmt = gson.fromJson(obj.get("clientMessageType"), ClientMessageType.class);
          }
          catch(Exception ex) {
             
          }
          try {
             smt = gson.fromJson(obj.get("serverMessageType"), ServerMessageType.class);  
          }
          catch(Exception ex) {
          }
          JsonObject data = obj.getAsJsonObject("data");
          MessageData messageData = MessageData.Deserialize(data);
                
          if (cmt != null)
             return new Message(messageData, cmt);
          
          return new Message(messageData, smt);   
       }
       catch(Exception ex) {
          System.out.println(ex.getLocalizedMessage());
          return null;
       }
       
       
    }
    public ServerMessageType getServerMessageType() {
       return serverMessageType;
    }
    public ClientMessageType getClientMessageType() {
       return clientMessageType;
    }
    
    /**
     * @return string of json serialized from Message class
     */
    public String serialized(){
       return gson.toJson(this);
   }
    public MessageData getMessageData() {
       return this.data;
    }
    public String toString() {
       StringBuilder builder = new StringBuilder();
       if (clientMessageType != null) builder.append("ClientMessageType : " + clientMessageType.toString() + "\n");
       if (serverMessageType != null) builder.append("ServerMessageType : " + serverMessageType.toString() + "\n");
       builder.append("MessageData is null: " + (data == null));
       
       if (data != null)
          builder.append("Message Data:\n" + data.toString());
       return builder.toString();
    }
}
