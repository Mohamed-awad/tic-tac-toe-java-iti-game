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
    ServerSession connection;
    Thread stServer;
//  public static  ArrayList<Data> playersInfo;  // array that get the data from data base it will be in thread so that it can be updated every while
    public static ArrayList<Player> onlinePlayers;
    public static ArrayList<Player> onlineBusyPlayers;
//        SignUp signUpRequest;
//        SignIn signInRequest;
//        public static ArrayList<SavedMultiGames>
//        public static ArrayList<SavedSingleGames>

    public Server() throws IOException {
        onlinePlayers = new ArrayList<>();
    }

    public void startServer() {
        try {
            gameServer = new ServerSocket(5000);
            System.out.println("Server is started");
            stServer = new Thread(() -> {
                while (true) {
                    try {
                        playerSocket = gameServer.accept();
                        new ServerSession(playerSocket);
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
            System.out.println("Server stopped");
        } catch (IOException ex) {
            System.out.println("error when closing server");
        }
    }
}
