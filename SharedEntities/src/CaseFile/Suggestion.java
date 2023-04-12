package CaseFile;

import Cards.Card;
import BoardEntities.*;

public class Suggestion extends CaseFile {

   // Attributes
   private boolean isAccusation = false;
   
   
   // Constructors 
   public Suggestion(Card[] cards, boolean isAccusation) {
      super(cards);
      setAccusation(isAccusation);
   }
   public Suggestion( Room room, GameCharacter character, Weapon weapon, boolean isAccusation) {
      super(room, character, weapon);
      setAccusation(isAccusation);
   }
    
   // Getters and Setters
   public boolean isAccusation() {
      return isAccusation;
   }

   public void setAccusation(boolean isAccusation) {
      this.isAccusation = isAccusation;
   }
}
