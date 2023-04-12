package General;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import BoardEntities.Board;

public class GameData {
    // attributes
    protected Board board;
    protected ArrayList<Player> players;
    protected int currTurnPlayerIdx = 0;
    public GameData() {
      board = new Board();
    }
    // protected
    public Board getBoard(){
        return this.board;
    }
    protected void setBoard(Board board){
        this.board = board;
    }
    protected void incrementPlayerIdx(){
        currTurnPlayerIdx = incrementIdx(currTurnPlayerIdx);
    }
    protected void decrementPlayerIdx(){
        currTurnPlayerIdx = decrementIdx(currTurnPlayerIdx);
    }
    
    /**
     * custom nested JSON deserializer
     * @param obj
     * @return
     */
    public static GameData Deserialize(JsonObject obj) {
       try {
          Gson gson = new Gson();
          ArrayList<Player> ps = new ArrayList<Player>();
          int currIdx = -1;
          
          if (obj.has("players")) {
             JsonArray playerArrayObj = obj.getAsJsonArray("players");
             for(int i = 0; i < playerArrayObj.size(); i++) {
                try {
                   ps.add(Player.Deserialize(playerArrayObj.get(i).getAsJsonObject()));  
                }
                catch(Exception ex) {
                   System.out.println(ex.getLocalizedMessage());
                }
             }
          }
          if (obj.has("currTurnPlayerIdx"))
             currIdx = obj.get("currTurnPlayerIdx").getAsInt();
          
          GameData gd = new GameData();
          gd.players = ps;
          gd.currTurnPlayerIdx = currIdx >= 0 ? currIdx : gd.currTurnPlayerIdx;
          return gd;   
       }
       catch(Exception ex) {
          System.out.println(ex.getCause());
          return null;
       }
    }
    
    // public methods
    public int incrementIdx(int currIdx){
        if (currIdx + 1 >= players.size()) currIdx = 0;
        else currIdx++;
        return currIdx;
    }
    public int decrementIdx(int currIdx){
        if (currIdx - 1 < 0){
            currIdx = players.size() - 1;
        }
        return currIdx;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
    public Player getCurrTurnPlayer() {
        return players.get(currTurnPlayerIdx);
    }
    public int getCurrTurnPlayerIdx() {
       return currTurnPlayerIdx;
    }
    public void setCurrTurnPlayer(int idx) {
       currTurnPlayerIdx = idx;
    }
    
    public String toString() {
       StringBuilder builder = new StringBuilder();
       builder.append("Board: \n" + board.toString());
       
       for (Player p : players) {
          builder.append("Player Character: " + p.getCharacter().getCharacterName() + "\n");
       }
       return builder.toString();
    }
}
