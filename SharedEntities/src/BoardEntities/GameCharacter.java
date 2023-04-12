package BoardEntities;

public class GameCharacter {
    // static fields
    public static String[] characters = "Miss Scarlet, Colonel Mustard, Mrs. White, Mr. Green, Mrs. Peacock, Prof. Plum".split(", ");
    
    // attributes
    private int characterNum = -1;
    
    // constructors
    public GameCharacter(int idx) {
       setCharacterNum(idx);
    }

   // getters and setters
   public int getCharacterNum() {
      return characterNum;
   }
   
   public String getCharacterName() {
      if (characterNum >= 0 && characterNum < characters.length) {
         return characters[characterNum];     
      }
      else return null;
   }

   public void setCharacterNum(int characterNum) {
      this.characterNum = characterNum;
   }
}
