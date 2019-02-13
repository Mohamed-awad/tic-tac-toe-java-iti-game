///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package newpackage;
//
//import server.*;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author Motaz
// */
//public class Server11 {
//
//        ServerSocket gameServer;
//        DataInputStream rsForName;
//        DataOutputStream outputStream;
//        ObjectInputStream receivingRequest;
////        public static  ArrayList<Data> playersInfo;  // array that get the data from data base it will be in thread so that it can be updated every while
//        public static ArrayList<Player> onlinePlayers;
//        public static ArrayList<Player> onlineBusyPlayers;
////        SignUp signUpRequest;
////        SignIn signInRequest;
////        public static ArrayList<SavedMultiGames>
////        public static ArrayList<SavedSingleGames>
//
//        public Server11() {
//                try {
//                        gameServer = new ServerSocket(15214);
//                        onlinePlayers = new ArrayList<>();
//                        Object request = new Object();
//                        new Thread(() -> {
//                                Socket playerSocket;
//                                String playerName;
//                                Player onlinePlayer;
//                                while(true){
//                                try {
//                                        playerSocket = gameServer.accept();                             
//                                        outputStream = new DataOutputStream(playerSocket.getOutputStream());
//                                        rsForName = new DataInputStream((playerSocket.getInputStream())); //read the name immediately after connection
//                                        //Hesham Part database signIn // if which will reply true or false 
//                                        playerName = rsForName.readUTF();
//                                        onlinePlayer = new Player(playerSocket, playerName,"idle"); // Each Player has socket and id
//                                        onlinePlayers.add(onlinePlayer);
//                                        Session  connection = new Session(onlinePlayer);
//                                        System.out.println("connected");
//                                        
//                                } catch (IOException ex) {
//                                        System.out.println("dddd");
//                                }
//                        }}).start();
//                } catch (IOException ex) {
//                        System.out.println("sssssssssssss");
//                        Logger.getLogger(Server11.class.getName()).log(Level.SEVERE, null, ex);
//                }
//        }
//        public static void main(String[] args) {
//                new Server11();
//        }
//       
//                
//                
//}
