package Cards;

import BoardEntities.GameCharacter;

public class CharacterCard extends Card {
   // attributes
   private GameCharacter GameCharacter;

   // constructors
   public CharacterCard(GameCharacter gameCharacter) {
      super(gameCharacter.getCharacterName(), CardType.Character);
      this.GameCharacter = gameCharacter;
      this.setIndex(gameCharacter.getCharacterNum());
   }
   
   // getters and setters
   public GameCharacter getGameCharacter() {
      return GameCharacter;
   }

   
   public void setGameCharacter(GameCharacter character) {
      GameCharacter = character;
   }
}
