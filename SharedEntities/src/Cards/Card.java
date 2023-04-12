package Cards;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Card {
   // enumerations
    public enum CardType{
        Room, Character, Weapon
    }
    
   
    // attributes
    private String Value;
    private int Index;
    private CardType Type;
    
    
    //constructors
    public Card() {}
    public Card(String value, CardType type) {
       setValue(value);
       setType(type);
    }
    
   
    // getters and setters
   public String getValue() {
      return Value;
   }
   public void setValue(String value) {
      Value = value;
   }
   public CardType getType() {
      return Type;
   }
   public void setType(CardType type) {
      Type = type;
   }
   public int getIndex() {
      return Index;
   }
   public void setIndex(int index) {
      Index = index;
   }
//   public static Card Deserialize(JsonObject obj) {
//      String val = "";
//      int idx = -1;
//      CardType type = null;
//      
//      Gson gson = new Gson();
//      if (obj.has("Value")) {
//         val = obj.get("Value").getAsString();
//      }
//      if (obj.has("Index")) {
//         idx = obj.get("Index").getAsInt();
//      }
//      if (obj.has("Type")) {
//         type = 
//      }
//      
//      
//   }
}
