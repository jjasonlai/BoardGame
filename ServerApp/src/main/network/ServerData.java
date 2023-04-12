package main.network;

import java.util.ArrayList;

import General.Player;
import main.game.Deck;
import Cards.*;
import CaseFile.CaseFile;
import General.GameData;

/**
 * Class holding the necessary data for the game, allows for methods
 * handling player suggestions and accusations.
 * @author Jason
 */
public class ServerData extends GameData {
   // attributes
   private Deck deck;
   private CaseFile primaryCaseFile;

   // constructors
   public ServerData() {
      deck = new Deck();
      primaryCaseFile = deck.createCaseFile();
      deck.shuffle();
   }

   // public methods
   public boolean verifyAgainstPrimaryCaseFile(CaseFile caseFile) {
      return primaryCaseFile.getRoom() == caseFile.getRoom()
            && primaryCaseFile.getCharacter() == caseFile.getCharacter()
            && primaryCaseFile.getWeapon() == caseFile.getWeapon();
   }

   public ArrayList<ArrayList<Card>> deal(int playerCount){
      return deck.deal(playerCount);
   }
   
   // private methods
   
   // NOTE: These methods probably should not be in this class but I didn't know where to put them.
   
   
   public Player findPlayerDisprovingSuggestion(CaseFile suggestion) {
      int start = this.currTurnPlayerIdx;
      for (int i = 0; i < players.size() - 1; i++) {
         start = incrementIdx(start);
         Card[] hand = players.get(start).getCards();
         for (Card card : hand) {
            if (suggestion.containsCard(card))
               return players.get(start);
         }
      }
      return null;
   }
   
   public ArrayList<Card> getCardsDisprovingSuggestion(Player player, CaseFile suggestion) {
      ArrayList<Card> out = new ArrayList<Card>();
      Card[] hand = player.getCards();
      for (Card card : hand) {
         if (suggestion.containsCard(card))
            out.add(card);
      }
      return out;
   }

   public void setNextTurn() {
      incrementPlayerIdx();
   }
   public void removePlayer(Player player) {
      ArrayList<Player> ps = new ArrayList<Player>();
      for (int i = 0; i < getPlayers().size(); i++) {
         if (ps.size() == 0) break;
         Player p = getPlayers().get(i);
         if (!p.equals(player) && p != null)
            ps.set(i, p);  
      }
      setPlayers(ps); 
   }
   
   public void updatePlayer(Player newPlayer) {
      ArrayList<Player> ps = getPlayers();
      for (int i = 0; i < getPlayers().size(); i++) {
         Player p = ps.get(i);
         if (p.getCharacter().getCharacterName().equals(newPlayer.getCharacter().getCharacterName())){
            p.setCurrentRoom(newPlayer.getCurrentRoom());
            ps.set(i, p);
         }  
      }
      setPlayers(ps);
   }
   
}
