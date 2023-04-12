package main.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import BoardEntities.GameCharacter;
import BoardEntities.*;
import BoardEntities.Room.RoomType;
import Cards.Card;
import CaseFile.Suggestion;
import General.*;
import Messaging.Message;
import Messaging.Message.ClientMessageType;
import Messaging.Message.ServerMessageType;
import Messaging.MessageData;
import main.network.ClientData;
import main.network.ClientMessager;
import create.gui.Gameframe;
/*
 * Class for Game Actions on the Client side
 * - making suggestions
 * - choosing where to move on the board
 */
public class ClientGameActions {
    private ClientData data; 
    private ClientMessager messager;
    public Gameframe outMsg = new Gameframe();
    
    public ClientGameActions() {
       this.data = new ClientData();
       this.messager = new ClientMessager(data);
    }
    public ClientGameActions(Suggestion suggestion, ClientData data) {
       this.data = data;
    }
   
   
   public void HandleIncomingMessage(Message message) {
	   
      if (message == null) {
         //System.out.println("No message");
    	 Gameframe.writetoBox("No message"); 
         return;
      }
      // Changed switch statement to support my older java version -Jason
      ServerMessageType type = message.getServerMessageType();
      switch (type) {
         case WaitingForPlayers:
        	 
            System.out.println("Waiting for more players to join before starting the game...");
        	Gameframe.writetoBox("Waiting for more players to join before starting the game...");
        	 break;
         case InitializeGame :
            System.out.println("Initializing Player and board");
        	Gameframe.writetoBox("Initializing Player and board");
            Initialize(message.getMessageData());
            break;
         case NewTurn :
            System.out.println("New Turn Started");
            Gameframe.writetoBox("New Turn Started");
            HandleTurn(message.getMessageData().getData());
            break;
         case ShowCard :
            System.out.println("Show Card");
            Gameframe.writetoBox("Show Card");
            DisplayShownCard(message.getMessageData().getShownCard());
            break;
         case PromptDisprovingCards :
            System.out.println("Pick a Card");
            Gameframe.writetoBox("Pick a Card");
            SelectDisprovingCard(message.getMessageData().getDisprovingCards());
            break;
         case ReportGameResults :
            ReportGameResults(message.getMessageData());
            break;
         default:
        	Gameframe.writetoBox("Unable to determine type of message"); 
            System.out.println("Unable to determine type of message");
      }
   }
   
   private void Initialize(MessageData messageData) {
      this.data.setPlayers(messageData.getData().getPlayers());
      this.data.setPlayer(messageData.getPlayer());

      this.data.setBoard(new Board(data.getPlayers()));
      this.data.setCurrTurnPlayer(messageData.getData().getCurrTurnPlayerIdx());
      Gameframe.writetoBox(data.getPlayer().toString());
      System.out.println(data.getPlayer().toString());
   }

   private void ReportGameResults(MessageData messageData) {
	  Gameframe.writetoBox("Reporting Game Results");
	  Gameframe.writetoBox(" ");
	  Gameframe.writetoBox("The winner is: " + messageData.getPlayer());
	  System.out.println("Reporting Game Results");
      System.out.println(" ");
      System.out.println("The winner is: " + messageData.getPlayer());
   }
   
   private void HandleTurn(GameData serverData) {
      // my turn
      UpdateData(serverData);
      Gameframe.writetoBox("Turn started!");
      System.out.println("Turn started!");
      if (isTurn()) {
    	 Gameframe.writetoBox(              
    		"Your current room: " 
            + data.getCurrTurnPlayer().getCurrentRoom().getRoomName() 
            + "\nSelect one number"); 
         System.out.println(
              "Your current room: " 
            + data.getCurrTurnPlayer().getCurrentRoom().getRoomName() 
            + "\nSelect one number"
         );
         
         // show valid moves
         ArrayList<Room> validMoves = data.getBoard().getAllValidMoves(data.getPlayer());
         for(int i = 0; i < validMoves.size(); i++) {
        	Gameframe.writetoBox(i + " " + validMoves.get(i).getRoomName()); 
            System.out.println(i + " " + validMoves.get(i).getRoomName());
         }
         
         // move to new room
         Integer i = forceNumberInputWithinMax(validMoves.size());
         Room newRoom = data.getBoard().getRoomByReference(validMoves.get(i));
         data.setPlayer(data.getBoard().move(data.getPlayer(), newRoom));
         
         if (data.getPlayer().getCurrentRoom().equals(newRoom))
        	Gameframe.writetoBox("Moved to: " + newRoom.getRoomName()); 
            System.out.println("Moved to: " + newRoom.getRoomName());
         

         Suggestion suggestion = null;
         // make suggestion or accusation
         if (data.getPlayer().getCurrentRoom().getRoomType() != Room.RoomType.Hallway) {
            System.out.println("Would you like to make a suggestion? Yes or No");
            Gameframe.writetoBox("Would you like to make a suggestion? Yes or No");
            try {
               if (newRoom.getRoomType() == RoomType.Room && messager.userInput.readLine().contains("y")) {
                  suggestion = MakeAccusationSuggestion();
               }
            } catch (IOException e) {
               e.printStackTrace();
            }
         }

         
         // send the result of the turn
         MessageData turnMessageData = new MessageData(data, suggestion);
         Message turnMessage = new Message(turnMessageData, ClientMessageType.EndTurn);
         messager.sendMessage(turnMessage);
      }
      else {
    	 Gameframe.writetoBox("Your Player is: " + data.getPlayer().getName() + "\n" + "Current Turn Player is: " + data.getCurrTurnPlayer().getName()); 
         System.out.println("Your Player is: " + data.getPlayer().getName());
         System.out.println("Current Turn Player is: " + data.getCurrTurnPlayer().getName());
      }
   }
   
   private Suggestion MakeAccusationSuggestion() {
	  Gameframe.writetoBox("Please choose a character\n\n"); 
      System.out.println("Please choose a character\n\n");
      for(int i = 0; i < GameCharacter.characters.length; i++) {
    	  Gameframe.writetoBox(Integer.toString(i) + GameCharacter.characters[i]);
    	  System.out.println(i + GameCharacter.characters[i]);
      }
      int charIdx = forceNumberInputWithinMax(GameCharacter.characters.length - 1);

      for(int i = 0; i < Weapon.weapons.length; i++) {
    	  Gameframe.writetoBox(Integer.toString(i) + Weapon.weapons[i] );
    	  System.out.println(i + Weapon.weapons[i]);
      }
      int weaponIdx = forceNumberInputWithinMax(Weapon.weapons.length - 1);
      
      Gameframe.writetoBox("Do you want to make this an accusation? Yes or no.");
      System.out.println("Do you want to make this an accusation? Yes or no.");
      boolean isAccusation = false;
      try{
         isAccusation = messager.userInput.readLine().contains("y");
      }
      catch(Exception ex){
    	 Gameframe.writetoBox(ex.getLocalizedMessage()); 
         System.out.println(ex.getLocalizedMessage());
      }
      
      GameCharacter character = new GameCharacter(charIdx);
      Weapon weapon = new Weapon(weaponIdx);
      Room room = data.getPlayer().getCurrentRoom();
      return new Suggestion(room, character, weapon, isAccusation);
   }
   
   private void DisplayShownCard(Card card) {
	  Gameframe.writetoBox("This card was shown to you by another player: " + card.getValue()); 
      System.out.println("This card was shown to you by another player: " + card.getValue());
   }
   
   private void SelectDisprovingCard(ArrayList<Card> disprovingCards) {
      Gameframe.writetoBox("Please choose one card");
	  System.out.println("Please choose one card");
      for(int i = 0; i < disprovingCards.size(); i++) {
    	 Gameframe.writetoBox(i + " " + disprovingCards.get(i).getValue()); 
         System.out.println(i + " " + disprovingCards.get(i).getValue());
      }
      int res = forceNumberInputWithinMax(disprovingCards.size() - 1);
      
      MessageData showCardData = new MessageData(disprovingCards.get(res));
      messager.sendMessage((new Message(showCardData, ClientMessageType.ShowCard)));
      
   }
   
   // helper region
   private void UpdateData(GameData serverData) {
      this.data.setBoard(new Board(serverData.getPlayers()));
      data.setCurrTurnPlayer(serverData.getCurrTurnPlayerIdx());
   }
   private boolean isTurn() {
      return data.getCurrTurnPlayer().getName().equals(data.getPlayer().getName());
   }
   private Integer forceNumberInputWithinMax(int max) {
      Integer i = -1;
      while (i < 0 || i >= max) {
         try {
        	Gameframe.writetoBox("What number do you want to pick? "); 
            System.out.println("What number do you want to pick? ");
            String userInput = messager.userInput.readLine();
            i = Integer.parseInt(userInput);
         }
         catch(Exception ex) {
            i = -1;
         }
      }
      return i;
   }
   public ClientMessager getMessager() {
      return messager;
   }
   
}
