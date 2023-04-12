package main;

import General.GameData;
import Messaging.Message;
import main.game.ClientGameActions;
import main.network.ClientData;
import main.network.ClientMessager;
import create.gui.Gameframe;
import java.io.IOException;
import java.util.Scanner;

public class ClueLessClient extends Thread {

   public void run() {
	     Gameframe outM = new Gameframe();
	     outM.writetoBox("Connecting to Server...");
         //System.out.println("Connecting to Server...");
         ClientGameActions gameManager = new ClientGameActions(); 
         
         ClientMessager messager = gameManager.getMessager();
         while (!gameManager.getMessager().isClosed()) {
            
            messager.listenToMessages();
            
            Message lastMessage = messager.getLastReceivedMessage();
            if (lastMessage != null)
               gameManager.HandleIncomingMessage(lastMessage);
         } 
      
   }
}
