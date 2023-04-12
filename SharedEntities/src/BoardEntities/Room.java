package BoardEntities;

import java.util.ArrayList;

import General.Player;

public class Room {
   // region static
   public static String[] rooms = "Study Hall Lounge Library Billiard Dining Conservatory Ballroom Kitchen".split(" ");
   public static int[][] connections = new int[][] {
      // row 1 horizontal
      {0, 1},
      {1, 2},

      // row 1 vertical
      {0, 3},
      {1, 4},
      {2, 5},

      // row 2 horizontal
      {3, 4},
      {4, 5},

      // row 2 vertical
      {3, 6},
      {4, 7},
      {5, 8},

      // row 3 horizontal
      {6, 7},
      {7, 8},
   };
   public static int[][] secretPassages = new int[][]{
      // Secret Passageways
      {0, 8},
      {2, 6}
   };
   public enum RoomType {
      Room, Hallway, SecretPassage
   }
   // endregion static

   // constructors
   public Room(int roomNameIdx, RoomType type){
      this.roomNum = roomNameIdx;
      switch(type){
         case Room:
            this.maxAllowedCharacters = 2;
            break;
         default:
            break;
      }
      this.roomType = type;
   }

   // attributes
   private int roomNum = -1;
   private ArrayList<Integer> connectingRooms = new ArrayList<Integer>();
   private Weapon containedWeapon;
   private int maxAllowedCharacters = 1;
   private ArrayList<GameCharacter> containedCharacters = new ArrayList<GameCharacter>();
   private RoomType roomType = RoomType.Room;
   
   // region Getters and Setters, add delete from lists
   /**
    * Find the key generated for the board HashMap
    */
   public int getKeyNum(){
//      if (roomType == RoomType.SecretPassage)
//         return - (roomNum + 1);

      return roomNum;
   }
   
   public String getRoomName() {
      if (roomNum >= 0 && roomNum < rooms.length && roomType == RoomType.Room) {
         return rooms[roomNum];
      }
      else if (roomType == RoomType.Hallway) {
         try {
            return rooms[connectingRooms.get(0)] + " to " + rooms[connectingRooms.get(1)];
         }
         catch(Exception ex) {
            System.out.println("Cannot get room name");
         }
      }
      return "Cannot Find Name";
   }
   public void addConnectingRoom(int idx){
      connectingRooms.add(idx);
   }
   public ArrayList<Integer> getConnectingRooms(){
      return this.connectingRooms;
   }
   public Weapon getContainedWeapon() {
      return containedWeapon;
   }
   public void setContainedWeapon(Weapon containedWeapon) {
      this.containedWeapon = containedWeapon;
   }
   /**
    * Warning - use sparingly for displaying contained characters. Use add, and remove methods for direct manipulation.
    * @return
    */
   public ArrayList<GameCharacter> getContainedCharacters() {
      return containedCharacters;
   }

   public void removeContainedCharacter(GameCharacter character){
      if (containedCharacters.contains(character)){
         containedCharacters.remove(character);
      }
   }
   public void removeContainedCharacter(int idx){
      if (containedCharacters.size() >= idx + 1){
         containedCharacters.remove(idx);
      }
   }
   public RoomType getRoomType() {
      return roomType;
   }
   // endRegion Getters and Setters

   public boolean isFull(){
      return containedCharacters.size() >= this.maxAllowedCharacters;
   }
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Name = " + getRoomName()); //  + "\nContainedCharacters: \n");
//      for(GameCharacter character : containedCharacters) {
//         builder.append("\tCharacter name: " + character.getCharacterName() + "\n");
//      }
//      builder.append("Connected Rooms: ");
//      for (int i : getConnectingRooms()) {
//         
//      }
      return builder.toString();
   }
}
