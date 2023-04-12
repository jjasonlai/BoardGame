package BoardEntities;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import General.Player;
import BoardEntities.Room.RoomType;

/**
 * Board holds rooms, characters, locations all as a graph of rooms.
 */
public class Board {

   // attributes
   private ArrayList<Room> rooms = new ArrayList<Room>();
   private HashMap<Integer, Room> connectingRooms = new HashMap<Integer, Room>();

   // constructors
   public Board() {
      buildBoard();
   }
   public Board(ArrayList<Player> players) {
      this();
      for (Player player : players) {
         Room roomRef = player.getCurrentRoom();
         place(player,roomRef);
         
      }
   }
   private void buildBoard(){
      // initialize rooms array
      for (int i = 0; i < Room.rooms.length; i++){
         rooms.add(new Room(i, RoomType.Room));
      }
      
      // add hallways, connect rooms to hallways
      for (int i = 0; i < Room.connections.length; i++){
         Room hallway = new Room(i, RoomType.Hallway);

         // from and to rooms
         int connectingRoom0 = Room.connections[i][0];
         int connectingRoom1 = Room.connections[i][1];

         // hallway references from and to rooms
         hallway.addConnectingRoom(connectingRoom0);
         hallway.addConnectingRoom(connectingRoom1);

         connectingRooms.put(hallway.getKeyNum(), hallway);

         // from room and to room reference hallway
         Room room0 = rooms.get(connectingRoom0);
         Room room1 = rooms.get(connectingRoom1);
         room0.addConnectingRoom(hallway.getKeyNum());
         room1.addConnectingRoom(hallway.getKeyNum());
         
         rooms.set(connectingRoom0, room0);
         rooms.set(connectingRoom1, room1);
      }

      // add secret passages, connect rooms to secret passages
//      for (int i = 0; i < Room.secretPassages.length; i++){
//         Room secretPassage = new Room(i, RoomType.SecretPassage);
//         // from and to rooms
//         int secretPassage0 = Room.secretPassages[i][0];
//         int secretPassage1 = Room.secretPassages[i][1];
//
//         // secret passage references from and to rooms
//         secretPassage.addConnectingRoom(secretPassage0);
//         secretPassage.addConnectingRoom(secretPassage1);
//         connectingRooms.put(secretPassage.getKeyNum(), secretPassage);
//
//         // from and to rooms reference secret passage
//         Room room0 = rooms.get(secretPassage0);
//         Room room1 = rooms.get(secretPassage1);
//         room0.addConnectingRoom(i);
//         room1.addConnectingRoom(i);
//      }
   }
   
   // public methods
   public Player move(Player player, Room toRoom){
      if (isValidMove(player.getCurrentRoom(), toRoom)){
         place(player, toRoom);
         return player;
      }
      System.out.println("Invalid move");
      return player;
   }
   public Player place(Player player, Room toRoom) {
      try {
         Room playerRefRoom = player.getCurrentRoom();
         Room currRoom = getRoomByReference(playerRefRoom);
         toRoom = getRoomByReference(toRoom);
         
         if (currRoom != null)
            currRoom.getContainedCharacters().remove(player.getCharacter());
         
         toRoom.getContainedCharacters().add(player.getCharacter());
         
         setRoomByReference(toRoom);
         player.setCurrentRoom(toRoom);   
      }
      catch(Exception ex) {
         System.out.println("Problem moving player, " + player.getName());
      }
      return player;
   }
   
   // private methods
   public Room getRoomByIndex(Integer idx, RoomType type){
      switch(type){
         case Room:
            return rooms.get(idx);
         case Hallway:
            return connectingRooms.get(idx);
         case SecretPassage:
            return connectingRooms.get(idx);
      }
      return null;
   }
   public Room getRoomByReference(Room room) {
      return getRoomByIndex(room.getKeyNum(), room.getRoomType());
   }
   public void setRoomByIndex(Integer idx, RoomType type, Room newRoom) {
      switch(type){
         case Room:
            rooms.set(idx,newRoom);
            break;
         case Hallway:
            connectingRooms.put(idx,newRoom);
            break;
         case SecretPassage:
            connectingRooms.put(idx,newRoom);
            break;
      }
   }
   
   public void setRoomByReference(Room newRoom) {
      setRoomByIndex(newRoom.getKeyNum(), newRoom.getRoomType(), newRoom);
   }
   
   private ArrayList<Room> getConnectingRooms(Room room){
      RoomType roomType = room.getRoomType();
      switch(roomType) {
      case Room:
         roomType = RoomType.Hallway;
         break;
      case Hallway:
         roomType = RoomType.Room;
         break;
      case SecretPassage:
         roomType = RoomType.Room;
         break;
      }
      ArrayList<Room> out = new ArrayList<Room>();
      for(int i = 0; i < room.getConnectingRooms().size(); i++){
         Room conRoom = getRoomByIndex(i, roomType);
         out.add(conRoom);
      }
      return out;
   }

   private boolean isValidMove(Room fromRoom, Room toRoom){
      fromRoom = getRoomByReference(fromRoom);
      if (!toRoom.isFull()){
         return true;
      } return false;
   }
   
   public ArrayList<Room> getAllValidMoves(Player player) {
      ArrayList<Room> conRooms = getConnectingRooms(getRoomByReference(player.getCurrentRoom()));
      for (int i = 0; i < conRooms.size(); i++) {
         Room room = conRooms.get(i);
         if (!isValidMove(player.getCurrentRoom(),room))
            conRooms.remove(room);
      }
      return conRooms;
   }
   
   public String toString() {
      StringBuilder builder = new StringBuilder();
      for (Room room : rooms) {
         builder.append(room.toString());
      }
      for (Room room : connectingRooms.values()) {
         builder.append(room.toString());
      }
      return builder.toString();
   }
}
