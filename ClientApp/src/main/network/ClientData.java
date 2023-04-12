package main.network;

import BoardEntities.Board;
import Cards.Card;
import General.GameData;
import General.Player;

/**
 * Class for all data relevant to the client.
 */
public class ClientData extends GameData {
    private Player player;
    
    public ClientData() {
       setBoard(new Board());
    }
    public ClientData(Player player, Board board) {
       setPlayer(player);
       setBoard(board);
    }
    
   public Player getPlayer() {
      return player;
   }
   public void setPlayer(Player player) {
      this.player = player;
   }
   public Board getBoard() {
      return board;
   }
   public void setBoard(Board board) {
      this.board = board;
   }
   
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Player: " + player.getCharacter().getCharacterName() + "\n");
      builder.append("Room: " + player.getCurrentRoom().getRoomName() + "\n");
      builder.append("Cards: \n");
      for(Card card: player.getCards()) {
         builder.append("\t" + card.getValue() + "\n");
      }
      return builder.toString();
   }
   
}
