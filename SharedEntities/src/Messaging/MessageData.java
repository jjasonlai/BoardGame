package Messaging;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import Cards.Card;
import CaseFile.Suggestion;
import General.GameData;
import General.Player;

/**
 *  Message data can only hold one piece of data
 *  READONLY
 * @author Jason
 */
public class MessageData {
   // READONLY
   private GameData data;
   private Suggestion suggestion;
   private ArrayList<Card> disprovingCards;
   private Card shownCard;
   private boolean startGame;
   private Player player;
   // ONLY ONE DATA FIELD ALLOWED
   // I tried to put multiple constructors that each only allow the one type of data.
   // this feels wrong

   // server sends turn data
   public MessageData(GameData data) {
      this.data = data;
   }
   // server sends initialization data
   public MessageData(GameData data, Player player) {
      this.data = data;
      this.player = player;
   }
   // player sends suggestion
   public MessageData(GameData data, Suggestion suggestion) {
      this.data = data;
      this.suggestion = suggestion;
   }
   // server sends disproving cards
   public MessageData(ArrayList<Card> disprovingCards) {
      this.disprovingCards = disprovingCards;
   }
   // player or server sends shown card
   public MessageData(Card shownCard) {
      this.shownCard = shownCard;
   }
   // player can early start the game
   public MessageData(boolean startGame) {
      this.startGame = startGame;
   }
   
   // getters
   public GameData getData() {
      return data;
   }
   public Suggestion getSuggestion() {
      return suggestion;
   }
   public ArrayList<Card> getDisprovingCards() {
      return disprovingCards;
   }
   public Card getShownCard() {
      return shownCard;
   }
   public boolean isStartGame() {
      return startGame;
   }
   public static MessageData Deserialize(JsonObject obj) {
      if (obj == null) return null;
      Gson gson = new Gson();
      
      JsonObject gameDataObj = null;
      GameData gameData = null;
      
      JsonObject suggObj = null;
      Suggestion sugg = null;
      
      JsonArray dispCardsArr = null;
      ArrayList<Card> dispCards = null;
      
      JsonObject showCardObj = null;
      Card card = null;

      JsonObject playerObj = null;
      Player p = null;
      
      
      if (obj.has("data")) {
         gameDataObj = obj.getAsJsonObject("data"); 
         gameData = GameData.Deserialize(gameDataObj); 
      }
      if (obj.has("suggestion")) {
         suggObj = obj.getAsJsonObject("suggestion"); 
         sugg = gson.fromJson(suggObj, Suggestion.class);
      }
      if (obj.has("disprovingCards")) {
         dispCardsArr = obj.getAsJsonArray("disprovingCards");
         dispCards = new ArrayList<Card>();
         for (int i = 0; i < dispCardsArr.size(); i++) {
            dispCards.add(gson.fromJson(dispCardsArr.get(i).getAsJsonObject(), Card.class));
         }
      }
      if (obj.has("shownCard")) {
         showCardObj = obj.getAsJsonObject("shownCard");
         card = gson.fromJson(showCardObj, Card.class);
      }
      if (obj.has("player")) {
         playerObj = obj.getAsJsonObject("player");
         p = Player.Deserialize(playerObj);      
      }
      
      if (dispCards != null)
         return new MessageData(dispCards);
      
      if (gameData != null) {
         if (p != null)
            return new MessageData(gameData,p);
         if (sugg != null)
            return new MessageData(gameData, sugg);
         
         return new MessageData(gameData);
      }
      return new MessageData(card);
   }
   
   public String toString() {
      StringBuilder builder = new StringBuilder();
      if (data != null) {
         builder.append("Game Data: " + data.toString());
      }
      if (suggestion != null) {
         builder.append("Suggestion: " + suggestion.toString());
      }
      if (disprovingCards != null) {
         builder.append("Disproving Cards: " + disprovingCards.toArray().toString());
      }
      if (shownCard != null) {
         builder.append("ShownCard" + shownCard.getValue());
      }
      if (startGame) {
         builder.append("Start Game == " + startGame);
      }
      return builder.toString();
   }
   public Player getPlayer() {
      return player;
   }
}
