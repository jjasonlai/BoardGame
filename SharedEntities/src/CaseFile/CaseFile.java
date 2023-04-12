package CaseFile;

import BoardEntities.*;
import BoardEntities.GameCharacter;
import BoardEntities.Room.RoomType;
import Cards.*;

public class CaseFile {
   // Attributes
   private Room room;
   private GameCharacter gameCharacter;
   private Weapon weapon;

   private Card[] Cards = new Card[3];

   // Constructors
   public CaseFile(Room room, GameCharacter character, Weapon weapon) {
      this.room = room;
      this.gameCharacter = character;
      this.weapon = weapon;
   }
   public CaseFile(Card[] cards) {
      this.Cards = cards;
      initializeFromCards();
      
   }
   
   // private methods
   private void initializeFromCards() {
      if (this.Cards.length > 0) {
         for (Card card: this.Cards) {
            switch(card.getType()) {
               case Room:
                  this.room = new Room(card.getIndex(), RoomType.Room);
                  break;
               case Character:
                  this.gameCharacter = new GameCharacter(card.getIndex());
                  break;
               case Weapon:
                  this.weapon = new Weapon(card.getIndex());
                  break;
               default:
                  System.out.println("Error in creation of CaseFile from cards");
                  break;
            }
         }
      }
   }
   

   public boolean containsCard(Card card){
      switch(card.getType()){
         case Character:
         return (this.getCharacter().getCharacterName().equals(new GameCharacter(card.getIndex()).getCharacterName()));
         case Weapon:
         return (this.getWeapon().getWeaponName().equals(new Weapon(card.getIndex()).getWeaponName()));
         case Room:
         return (this.getRoom().getRoomName().equals(new Room(card.getIndex(),RoomType.Room).getRoomName()));
      }
      return false;
   }


   // getters and setters
   public Room getRoom() {
      return room;
   }

   public void setRoom(Room room) {
      this.room = room;
   }

   public GameCharacter getCharacter() {
      return gameCharacter;
   }

   public void setCharacter(GameCharacter character) {
      gameCharacter = character;
   }

   public Weapon getWeapon() {
      return weapon;
   }

   public void setWeapon(Weapon weapon) {
      this.weapon = weapon;
   }

   public Card[] getCards() {
      return Cards;
   }

   public void setCards(Card[] cards) {
      Cards = cards;
   }
}
