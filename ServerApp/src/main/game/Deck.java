package main.game;

import java.util.ArrayList;
import java.util.Random;

import BoardEntities.*;
import BoardEntities.Room.RoomType;
import Cards.*;
import CaseFile.CaseFile;

public class Deck {
   // attributes
   private ArrayList<Card> deck = new ArrayList<Card>();

   // constructors
   public Deck() {
      generateDeck();
      shuffle();
   }

   // public methods
   /**
    * @precondition the deck is shuffled
    * @return the primary case file
    */
   public CaseFile createCaseFile() {
      Card[] caseFileCards = new Card[3];
      int count = 0;
      while (caseFileCards[0] == null || caseFileCards[1] == null || caseFileCards[2] == null) {
         Card card = deck.get(count);
         switch (card.getType()) {
            case Character:
               if (caseFileCards[0] == null)
                  caseFileCards[0] = takeCardFromDeck(card);
               break;
            case Weapon:
               if (caseFileCards[1] == null)
                  caseFileCards[1] = takeCardFromDeck(card);
               break;
            case Room:
               if (caseFileCards[2] == null)
                  caseFileCards[2] = takeCardFromDeck(card);
               break;
         }
         count++;
      }
      return new CaseFile(caseFileCards);
   }
   public ArrayList<ArrayList<Card>> deal(int playerCount) {
      ArrayList<ArrayList<Card>> allPlayersCards = new ArrayList<ArrayList<Card>>();
      for (int i = 0; i < playerCount; i++) {
         allPlayersCards.add(new ArrayList<Card>());
      }
      int count = 0;
      while (count < deck.size()) {
         for(int i = 0; i < playerCount; i++) {
            allPlayersCards.get(i).add(deck.get(count));
            count++;
         }
      }
      deck = new ArrayList<Card>();
      return allPlayersCards;
   }
   public void shuffle() {
      Random rnd = new Random();

      for (int i = 0; i < deck.size(); i++) {
         // get 2 random indices
         int idx0 = rnd.nextInt(deck.size());
         int idx1 = rnd.nextInt(deck.size());

         // swap vals at those indices
         Card valAtIdx1 = deck.get(idx1);
         deck.set(idx1, deck.get(idx0));
         deck.set(idx0, valAtIdx1);
      }
   }

   
   // private methods
   private void generateDeck() {
      String[] characters = GameCharacter.characters;
      String[] weapons = Weapon.weapons;
      String[] rooms = Room.rooms;
      
      for (int i = 0; i < characters.length; i++) {
         deck.add(new CharacterCard(new GameCharacter(i)));
      }
      for (int i = 0; i < weapons.length; i++) {
         deck.add(new WeaponCard(new Weapon(i)));
      }
      for (int i = 0; i < rooms.length; i++) {
         deck.add(new RoomCard(new Room(i,RoomType.Room)));
      }
      
   }
   private Card takeCardFromDeck(Card card) {
      deck.remove(card);
      return card;
   }
}
