package main.network;

import java.io.*;
import java.net.Socket;
import Messaging.Message;

interface MessageRecieverHandler {
    public void onRecieveClientMessage(Socket socket, String message);
    public void onRecieveClientMessage(Socket socket, Message message);
}

public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private MessageRecieverHandler messagesRecieverHandler;

    public ServerThread(Socket socket, MessageRecieverHandler messagesRecieverHandler) {
        try {
            this.socket = socket;
            this.messagesRecieverHandler = messagesRecieverHandler;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            closeConnection(socket, output, input);
        }
    }

    @Override
    public void run() {
        super.run();
        String messageFromClient;
        try {
            messageFromClient = input.readLine();
            while(!socket.isClosed()) {
                handleClientMessages(messageFromClient);
                messageFromClient = input.readLine();
            }
        } catch (IOException e) {
            System.out.println("Client error terminated abruptly.");
        } catch(NullPointerException e) {
            System.out.println("Client Closed!");
        } finally {
            closeConnection(socket, output, input);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void closeConnection(Socket socket, PrintWriter printWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }

            if(printWriter != null) {
                printWriter.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            closeConnection(socket, printWriter, bufferedReader);
        }
    }

    public void sendMesageToClient(String message) {
        output.println(message);
        output.flush();
    }

    public void sendMesageToClient(Message message) {
        output.println(message.serialized());
        output.flush();
    }

    private void handleClientMessages(String message) {
       // System.out.println(message);
       try {
          Message incomingMessage = Message.Deserialize(message);
          messagesRecieverHandler.onRecieveClientMessage(socket, incomingMessage);
       }
       catch(Exception ex){
          messagesRecieverHandler.onRecieveClientMessage(socket, message);
       }
    }
}
