package Cards;

import BoardEntities.Room;

public class RoomCard extends Card {
   
   // attributes
   private Room room;
   
   // constructors
   public RoomCard(Room room) {
      super(room.getRoomName(), CardType.Room);
      setIndex(room.getKeyNum());
   }



   // getters and setters
   public Room getRoom() {
      return room;
   }

   public void setRoom(Room room) {
      this.room = room;
   }   
}

