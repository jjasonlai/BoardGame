package Messaging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import General.GameData;

public abstract class Messager {
   protected GameData data;
   protected int port;
   
   public Messager(GameData data, int port) {
      this.data = data;
      this.port = port;
   }
   

}