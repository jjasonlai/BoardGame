package main.game;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import BoardEntities.*;
import BoardEntities.Room.RoomType;
import Cards.Card;
import CaseFile.*;
import General.*;
import Messaging.Message.*;
import Messaging.*;
import main.network.*;

/**
 * Handles the game loop from creating the messaging/networking classes to running the game logic
 * and saving and sending data
 * @author Jason
 */
public class ServerGameManager {
   private ServerMessager messager;
   private ServerData data;
   private Player winningPlayer;

   public static final int MAX_PLAYERS_LIMIT = 2;

   public ServerGameManager() {
      data = new ServerData();
      messager = new ServerMessager(data); // step 1: start server
   }

   public ServerGameManager(ServerMessager messager) {
      data = new ServerData();
      this.messager = messager; // step 1: start server
   }

   // step 2: connect players to server
   /**
    * TODO: Implement and Test
    * Play the game, including all messaging and connections between server and client
    */
   public void playGame() 
   { 
      try {
         data.setPlayers(createPlayers()); // step 1 - connect players
         System.out.println("Clue Initializing");
         initializeGame();
         while(!gameOver()) {
            startNewTurn(); // step 2 - tell players whose turn it is
            handlePlayerTurn(); // step 3 - handle player move, suggestion, accusation
            data.setNextTurn();
         }
         endGame(); // step 4 - end game
      }
      catch(Exception ex) {
         System.out.println(ex.getMessage());
         System.out.println(ex.getStackTrace().toString());
      }
   }
   
   /**
    * Wait for players to connect to our server socket. Hold these clients
    * in a list of sockets. Generate GameCharacters for each client. 
    * @return list of players containing [name, character, room, socket reference].
    */
   private ArrayList<Player> createPlayers() {
      System.out.println("Creating Players");
      ArrayList<Socket> playerSockets = messager.getClients();
      ArrayList<Player> players = new ArrayList<Player>(); 

      ArrayList<ArrayList<Card>> dealtCards = data.deal(playerSockets.size());
      
      // Create a Player object for each socket. Each player has a reference to the socket
      for(int i = 0; i < playerSockets.size(); i++) {
         Card[] cards = dealtCards.get(i).toArray(new Card[dealtCards.get(i).size()]);
         GameCharacter character = new GameCharacter(i);
         
         // TODO: make room be in the proper spawn order
         Room room = data.getBoard().getRoomByIndex(i, RoomType.Room);
         Player player =  new Player(
               "Player" + (i + 1),   // name
               cards,                // hand/cards
               character,            // char
               room,                 // room
               playerSockets.get(i)  // socket
               );
         
         players.add(player);
         data.getBoard().place(player, player.getCurrentRoom());
      }
      return players; // return the list of players
   }
   
   /**
    * Send all players their newly created Player instance.
    */
   private void initializeGame() {
      ArrayList<Player> players = data.getPlayers();
      for (int i = 0; i < players.size(); i++) {
         MessageData messageData = new MessageData(data,players.get(i));
         messager.sendMessage(new Message(messageData, ServerMessageType.InitializeGame), players.get(i));
      }
   }
   
   // step 3: start turn
   /**
    * Find the player whose turn it is. Message players about whose turn it is.
    * The client that sees it is the current turn player will make moves on the client
    * side.
    */
   private void startNewTurn() {
      System.out.println("Turn Starting");
      
      MessageData messageData = new MessageData(this.data);
      messager.sendMessage(new ServerMessage(messageData, ServerMessageType.NewTurn));

   }
   
   // step 4: what did the player do on their turn?
   /**
    * When a client has finished their turn, they will send a message to the server.
    * The server will need to know:
    *    a. what room the player moved to
    *    b. whether or not the player made a suggestion
    *       - if the player made a suggestion, we move on to the next step: handling the suggestion
    */
   private void handlePlayerTurn() {
      Player player = data.getCurrTurnPlayer();
      Message message = messager.forceReceiveMessageOfType(ClientMessageType.EndTurn, player); // wait for player to send end turn data

      // update data from message
      
      GameData receivedData = message.getMessageData().getData();
      Player newPlayer = receivedData.getCurrTurnPlayer();
      data.updatePlayer(newPlayer); /// update player to the new data
      data.getBoard().place(player, newPlayer.getCurrentRoom()); // update board to reflect move of player
      
      
      // step 5: if the player made a suggestion, handle the suggestion
      Suggestion suggestion = message.getMessageData().getSuggestion();
      if (suggestion != null) {
         if(suggestion.isAccusation()) {
            System.out.println("Player made an accusation");
            handlePlayerAccusation(suggestion, player);
         }
         else {
            System.out.println("Player made a suggestion");
            handlePlayerSuggestion(suggestion);
         }
      }
   }
   
   // step 5: if the player made a suggestion, handle the suggestion
   /**
    * In order of players, find a player that has one or more cards contained in the suggestion.
    *    a. if a player is found, find all cards in the player's hand that are in the suggestion
    *    b. take these cards and send a message with them to the client.
    *       - client side will choose one of the cards and send back to server
    *    c. server will send the chosen card to the player that made the suggestion
    * 
    * @param suggestion
    */
   public void handlePlayerSuggestion(CaseFile suggestion) {
      Player nextDisprovingPlayer = data.findPlayerDisprovingSuggestion(suggestion);
      if (nextDisprovingPlayer == null) return;
      else if (nextDisprovingPlayer == data.getCurrTurnPlayer()) return;
      
      // get all the necessary cards from the disproving player
      ArrayList<Card> disprovingCards = data.getCardsDisprovingSuggestion(nextDisprovingPlayer, suggestion);
   
      // ask the disproving player to get a card. Wait for that card
      MessageData disproveData = new MessageData(disprovingCards);
      messager.sendMessage(new Message(disproveData, ServerMessageType.PromptDisprovingCards), nextDisprovingPlayer);
      Message message = messager.forceReceiveMessageOfType(ClientMessageType.ShowCard, nextDisprovingPlayer);
      
      // send the card to the player whose turn it is
      Card shownCard = message.getMessageData().getShownCard();
      MessageData shownCardData = new MessageData(shownCard);
      if (shownCard != null) messager.sendMessage(new Message(shownCardData, ServerMessageType.ShowCard), data.getCurrTurnPlayer());
   }
   
   /**
    * If the player guessed correctly, place them in the winning player field
    * else remove them from players - OR - find a way to eliminate players without
    * preventing them from receiving messages about the game.
    * @param accusation
    * @param player
    */
   private void handlePlayerAccusation(CaseFile accusation, Player player) {
      boolean gameWon = data.verifyAgainstPrimaryCaseFile(accusation);
      // TODO: do we message everyone the outcome?
      if (gameWon) this.winningPlayer = player;
      
      data.removePlayer(player);
 
   }
   
   /**
    * TODO: a and b
    * When the game ends, call this method to close all connections on the server side, message the players
    * and shut the program down
    */
   private void endGame() {
      // a. message all players the outcome of the game
      // b. close server and let program come to an end
      MessageData messageData = new MessageData(this.data, winningPlayer);
      messager.sendMessage(new ServerMessage(messageData, ServerMessageType.ReportGameResults));
      System.out.println("Thanks for playing!");
      messager.stopServer();
   }
   
   /**
    * @return whether the game is over or not
    */
   private boolean gameOver() {
      int count = 0;
      for(Player p : data.getPlayers()) if (p != null) count++;
      if (count == 0) return true;
      if (winningPlayer != null) return true;
      return false;
   }



}
