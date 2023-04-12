package main;

import main.game.ServerGameManager;
import main.network.ServerData;
import main.network.ServerMessager;
import java.io.*;

public class ClueLessServer {
   public static void main(String[] args) throws IOException {
      ServerGameManager manager = new ServerGameManager();
      manager.playGame();
   }
}
