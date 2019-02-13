//package newpackage;
//import server.*;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
///**
// *
// * @author Motaz
// */
//import java.io.DataOutputStream;
//public class Sessionn extends Thread{
//        DataInputStream  recievingStream;
//        DataOutputStream sendingStream;
//        DataInputStream  p2RecievingStream;
//        DataOutputStream p2SendingStream;
//        String secondPlayerName;
//        boolean accepted;
//        int mode; // 1 for single 2 for multi
//        MultiplayerGame game;
//        
//        
//        
//        
//        //In this constructor i create recieveing from player
//        public Sessionn(Player playerOne ) {
//                try {
//                        recievingStream = new DataInputStream(playerOne.playerSocket.getInputStream());
//                        sendingStream = new DataOutputStream(playerOne.playerSocket.getOutputStream());
//                        
//
//
//                        mode = recievingStream.readInt();
//                        System.out.println(mode);
//                                if ( mode == 1 ){
//                                        //create object of single player and send the socket to it
//                                }
//                                else{
//                                        System.out.println("invitation");
//                                        //wait for the user to choose invitation
//                                        secondPlayerName = recievingStream.readUTF();
//                                        System.out.println(secondPlayerName);
//                                        Server.onlinePlayers.forEach(playerTwo  -> {
//                                                if(secondPlayerName.equals(playerTwo.playerName)){
//                                                        System.out.println("player got");
//                                                        try {
//                                                                p2SendingStream = new DataOutputStream(playerTwo.playerSocket.getOutputStream());
//                                                                p2RecievingStream = new DataInputStream(playerTwo.playerSocket.getInputStream());
//                                                                p2SendingStream.writeUTF(playerOne.playerName); // send the name of the player through client
//                                                                accepted = p2RecievingStream.readBoolean(); // recieve the reply
//                                                                if (accepted){
//                                                                        game = new MultiplayerGame(playerOne, playerTwo);
//                                                                        // Changing the state of the player so that they don't get any more invitations
//                                                                        playerOne.playerState  = playerTwo.playerState= "playing"; 
//                                                                        Server.onlineBusyPlayers.add(playerOne);
//                                                                        Server.onlineBusyPlayers.add(playerTwo);
//                                                                        Server.onlinePlayers.remove(playerOne);
//                                                                        Server.onlinePlayers.remove(playerTwo);
//                                                                }
//                                                        } catch (IOException ex) {
//                                                                Logger.getLogger(Sessionn.class.getName()).log(Level.SEVERE, null, ex);
//                                                        }
//                                                }
//                                        });
//                                }
//                } catch (IOException ex) {
//                        Logger.getLogger(Sessionn.class.getName()).log(Level.SEVERE, null, ex);
//                }
//    }
//}
