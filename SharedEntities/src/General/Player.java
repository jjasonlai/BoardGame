package General;

import java.net.Socket;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import Cards.Card;
import BoardEntities.Room;
import BoardEntities.GameCharacter;

public class Player {
    private String name;
    private Card[] cards;
    private GameCharacter character;
    private Room currentRoom;
    private transient Socket socket;
    
    public Player(String name, Card[] hand, GameCharacter character, Room currentRoom, Socket socket) {
       setName(name);
       setCards(hand);
       setCharacter(character);
       setCurrentRoom(currentRoom);
       this.socket = socket;
    }
    public Player(String name, Card[] hand, GameCharacter character, Room currentRoom) {
       setName(name);
       setCards(hand);
       setCharacter(character);
       setCurrentRoom(currentRoom);
    }
    
   public static Player Deserialize(JsonObject obj) {
      if (obj == null) return null;
      Gson gson = new Gson();
      String name = "";
      Card[] c = null;
      GameCharacter chr = null;
      Room rm = null;
      try {
         if (obj.has("name"))
            name = obj.get("name").getAsString();  
      }
      catch(Exception ex) {
         System.out.println(ex.getLocalizedMessage());
      }
      if (obj.has("cards")) {
         JsonArray cardArrayObj = obj.getAsJsonArray("cards");
         c = new Card[cardArrayObj.size()];
         for (int i = 0; i < cardArrayObj.size(); i++) {
            try {
               c[i] = gson.fromJson(cardArrayObj.get(i).getAsJsonObject(), Card.class);
            }
            catch(Exception ex) {
               System.out.println(ex.getLocalizedMessage());
            }
         }
      }
      if (obj.has("character"))
         chr = gson.fromJson(obj.get("character"), GameCharacter.class);
      if (obj.has("currentRoom"))
         rm = gson.fromJson(obj.get("currentRoom"), Room.class);
      
      return new Player(name, c, chr, rm);
   }
    public Socket getSocket() {
       return this.socket;
    }
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public Card[] getCards() {
      return cards;
   }
   public void setCards(Card[] cards) {
      this.cards = cards;
   }
   public GameCharacter getCharacter() {
      return character;
   }
   public void setCharacter(GameCharacter character) {
      this.character = character;
   }
   public Room getCurrentRoom() {
      return currentRoom;
   }
   public void setCurrentRoom(Room currentRoom) {
      this.currentRoom = currentRoom;
   }
   public String toString() {
      StringBuilder builder = new StringBuilder();
      if (name != null)
         builder.append("Name: " + name + "\n");
      if (character != null) builder.append("Character: " + character.getCharacterName()+ "\n");
      if (currentRoom != null) builder.append("Room: " + currentRoom.getRoomName()+ "\n");
      if (cards != null) 
      {
         builder.append("Cards: "+ "\n");
         for(Card card : cards) {
            builder.append("\t" + card.getValue()+ "\n");  
         }
      }
      return builder.toString();
   }
}

