package main.network;

import java.io.IOException;
import java.net.Socket;
import General.GameData;
import Messaging.Message;
import Messaging.Messager;
import java.io.*;
import create.gui.Gameframe;
/**
 * Class that abstracts sending data to the Server
 * Handles creating and connecting Sockets to ServerSockets
 */
public class ClientMessager extends Messager {
   private Socket clientSocket;
   public BufferedReader userInput;
   public BufferedReader serverInput;
   private PrintWriter output;
   private Message LastReceivedMessage;
   //public Gameframe outputMsg = new Gameframe();
   public ClientMessager(ClientData clientData, int serverPort) {
      super(clientData, serverPort);
      try {
         this.clientSocket = new Socket("localhost", serverPort);
         this.userInput = new BufferedReader(new InputStreamReader(System.in));
         //this.userInput = Gameframe.inputMsg;
         this.serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         //this.serverInput = outputMsg.inputMsg
         this.output = new PrintWriter(clientSocket.getOutputStream());
      } catch (IOException ex) {
    	 
    	 //Gameframe.writetoBox(ex.getMessage());
         System.out.println(ex.getMessage());
         closeConnection(clientSocket, output, userInput);
      
      }
   }
   public ClientMessager(ClientData clientData) {
      this(clientData, 4999);
   }

   public Socket getClientSocket() {
      return clientSocket;
   }

   public void closeConnection(Socket socket, PrintWriter printWriter, BufferedReader bufferedReader) {
      try {
         if(bufferedReader != null) {
            bufferedReader.close();
         }

         if(printWriter != null) {
            printWriter.close();
         }

         if(socket != null) {
            socket.close();
         }
      } catch (IOException ex) {
    	 //Gameframe.writetoBox(ex.getMessage()); 
         System.out.println(ex.getMessage());
      }
   }

   private boolean shouldEndSession(String message) {
      if (message.toUpperCase().compareTo("QUIT") != 0) return false;
      return true;
   }
   
   public void listenToMessages() {
      if (!clientSocket.isClosed()) {
         try {
            String input = serverInput.readLine();
            if (input.length() > 0) {
            	//Gameframe.writetoBox("Message received");
               System.out.println("Message received");
               saveLastSentMessage(input);
            }
            else {
               //Gameframe.writetoBox("nothing sent from server yet");
               System.out.println("nothing sent from server yet");
            }
         }
         catch(Exception ex) {
        	//Gameframe.writetoBox("Message was empty");
            System.out.println("Message was empty");
         }
      }
      else {
    	 //Gameframe.writetoBox("Client connection is closed"); 
         System.out.println("Client connection is closed");
      }
   }
   
   private void saveLastSentMessage(String json) {
      try {
         this.LastReceivedMessage = Message.Deserialize(json);
      }
      catch(Exception ex) {
    	  
         System.out.println("Json not sent to client");
         System.out.println("Message sent\n" + json);
      }
   }

   public Message getLastReceivedMessage() {
      return LastReceivedMessage;
   }

   public void sendMessage(Message message) {
      try {
         output.println(message.serialized());
         output.flush();
      }
      catch(Exception ex) {
         System.out.println("ClientMessager sendMessage() failed");
         System.out.println(ex.getCause());
      }
   }
   public void sendMessage(String s) {
      output.write(s);
      output.flush();
   }
   public boolean isClosed() {
      return clientSocket.isClosed();
   }
}
