package main.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import General.GameData;
import General.Player;
import Messaging.Message;
import Messaging.MessageData;
import main.game.ServerGameManager;
import Messaging.Messager;
import Messaging.Message.ClientMessageType;


public class ServerMessager extends Messager implements MessageRecieverHandler {
   
   // attributes
   private HashMap<Integer,ArrayList<Message>> incomingMessages = new HashMap<Integer,ArrayList<Message>>();
   private ServerSocket serverSocket;
   private ArrayList<Socket> clients = new ArrayList<Socket>();
   private ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();
   
   // constructors
   public ServerMessager(ServerData data) {
      super(data, 4999);
      startServer();
   }

   public ServerMessager(ServerData data, int serverPort) {
      super((GameData)data, serverPort);
      startServer();
   }

   
   /**
    * Decode any message from a socket to 
    * @param s
    * @return
    */
   public Message receiveMessage(Socket s) {
      int idx = clients.indexOf(s);
      ArrayList<Message> lst = incomingMessages.get(idx);

      if (lst.size() == 0) return null;
      return lst.remove(0);
   }
   

   /**
    * Send message to one specified player
    * @param message
    * @param player
    */
   public void sendMessage(Message message, Player player) {
      Socket socket = player.getSocket();
      for (ServerThread serverThread: serverThreads) {
         if(serverThread.getSocket().equals(socket)) {
            serverThread.sendMesageToClient(message);
         }
      }
   }
   

   /**
    * Send message to all players
    * @param message
    */
   public void sendMessage(Message message) {
      for (ServerThread serverThread: serverThreads) {
         serverThread.sendMesageToClient(message.serialized());
      }
   }

   /**
    * Send message to all players
    * @param message String to send to socket.
    */
   public void sendMessage(Socket socket, Message message) {
      for (ServerThread serverThread: serverThreads) {
         if(serverThread.getSocket().equals(socket)) {
            serverThread.sendMesageToClient(message);
         }
      }
   }
   
   public ArrayList<Socket> getClients() {
      return this.clients;
   }

   public ArrayList<ServerThread> getServerThreads() {
      return this.serverThreads;
   }
   
   public void startServer() {
      System.out.println("Starting Clueless Server...");
      try {
         serverSocket = new ServerSocket(port);
         System.out.println("Server running in port: " + port);
         while(!serverSocket.isClosed() && clients.size() < ServerGameManager.MAX_PLAYERS_LIMIT) {
            Socket socket = serverSocket.accept();
            clients.add(socket);
            System.out.println("Client connection established!");

            ServerThread serverThread = new ServerThread(socket, this);
            serverThreads.add(serverThread);
            serverThread.start();
            Thread.sleep(2 * 1000);

            if(clients.size() < ServerGameManager.MAX_PLAYERS_LIMIT){
               MessageData messageData = new MessageData(false);
               Message message = new Message(messageData, Message.ServerMessageType.WaitingForPlayers);
               sendMessage(socket, message);
            }
         }
         for(int i = 0; i < clients.size(); i++) {
            incomingMessages.put(i, new ArrayList<Message>());
         }
      } catch(Exception ex) {
         System.out.println(ex.getMessage());
         stopServer();
      }
   }
   
   public Message forceReceiveMessageOfType(ClientMessageType type, Player player) {
      Message message = null;
      while (message == null) {
         try {
            TimeUnit.SECONDS.sleep(1);
            Message tryGetMessage = receiveMessage(player.getSocket());
            if (tryGetMessage != null)
               message = tryGetMessage.getClientMessageType() == type ? tryGetMessage: message;
         
         }catch(Exception ex) {
            System.out.println(ex.getMessage());
         }
      }
      return message;
   }
   
   public void stopServer() {
      try {
         if(serverSocket != null) {
            serverSocket.close();
         }
      } catch (IOException ex) {
         System.out.println(ex.getMessage());
      }
   }

   @Override
   public void onRecieveClientMessage(Socket socket, String message) {
      // TODO: Handle message revievment
   }

   @Override
   public void onRecieveClientMessage(Socket socket, Message message) {
      int idx = clients.indexOf(socket);
      if (incomingMessages.get(idx) == null) incomingMessages.put(idx, new ArrayList<Message>());
      ArrayList<Message> lst = incomingMessages.get(idx);
      lst.add(message);
      incomingMessages.put(idx, lst);
      System.out.println(incomingMessages.get(idx));
   }
}
