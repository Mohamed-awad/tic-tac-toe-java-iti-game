package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.assets.Request;
import server.assets.RequestType;

public class Server {

    ObjectOutputStream sendingStream;
    ArrayList<ObjectOutputStream> clients = new ArrayList<>();
    ServerSocket gameServer;
    DataInputStream rsForName;
    DataOutputStream outputStream;
    DataInputStream receivingRequest;
    Socket playerSocket;
    public ServerSession connection;
    Thread stServer;
//  public static  ArrayList<Data> playersInfo;  // array that get the data from data base it will be in thread so that it can be updated every while
    public static ArrayList<Player> onlinePlayers = new ArrayList<>();
//        SignUp signUpRequest;
//        SignIn signInRequest;
//        public static ArrayList<SavedMultiGames>
//        public static ArrayList<SavedSingleGames>
    public void startServer() {
        try {
            gameServer = new ServerSocket(5000);
            System.out.println("Server is started");
            stServer = new Thread(() -> {
                while (true) {
                    try {
                        playerSocket = gameServer.accept();
                        // we get the output stream for all sockets to control what will server send to all
                        sendingStream = new ObjectOutputStream(playerSocket.getOutputStream());
                        clients.add(sendingStream);
                        connection = new ServerSession(playerSocket, sendingStream);
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
            for (int i = 0; i < clients.size(); i++) {
                System.out.println("let's close the server");
                Request request = new Request(RequestType.SERVER_DISCONNECTED);
                clients.get(i).writeObject(request);
                System.out.println("server closed");
            }
            clients.clear();
            gameServer.close();
        } catch (IOException ex) {
            System.out.println(ex);
            System.out.println("error when closing server");
        }
    }
}
