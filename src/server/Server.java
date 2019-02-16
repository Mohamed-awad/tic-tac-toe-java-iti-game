package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    ServerSocket gameServer;
    DataInputStream rsForName;
    DataOutputStream outputStream;
    DataInputStream receivingRequest;
    Socket playerSocket;
    public ServerSession connection;
    Thread stServer;
    public static ArrayList<Player> onlinePlayers = new ArrayList<>();
    public static ArrayList<Player> onlineBusyPlayers; 

    public void startServer() {
        try {
            gameServer = new ServerSocket(5000);
            System.out.println("Server is started");
            stServer = new Thread(() -> {
                while (true) {
                    try {
                        playerSocket = gameServer.accept();
                        connection = new ServerSession(playerSocket);
                        System.out.println("connectionDone");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.out.println("not accepted");
                    }
                }
            });

            stServer.start();
        } catch (IOException ex) {
//            Logger.getLogger(ServerFunctoins.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopServer() {
        try {
            stServer.stop();
            gameServer.close();
            if(!Server.onlinePlayers.isEmpty())
            {
            	connection.disconnectServer();
            }
            System.out.println("Server stopped");
        } catch (IOException ex) {
            System.out.println("error when closing server");
        }
    }
}
