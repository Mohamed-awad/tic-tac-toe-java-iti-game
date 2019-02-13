package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import server.assets.Request;
import server.assets.RequestType;

public class ServerSession extends Thread {

    Player onlinePlayer;
    Player playerTwo;
    Socket playerSocket;
    ObjectInputStream recievingStream;
    ObjectOutputStream sendingStream;
    Request request;
    ObjectInputStream p2RecievingStream;
    ObjectOutputStream p2SendingStream;

    //In this constructor i create recieveing from player
    public ServerSession(Socket ps) throws IOException {
        playerSocket = ps;
        recievingStream = new ObjectInputStream(ps.getInputStream());
        sendingStream = new ObjectOutputStream(ps.getOutputStream());
        start();
    }

    public void run() {
        while (true) {
            try {
                request = (Request) recievingStream.readObject();
                requestHandler(request);
            } catch (IOException ex) {
                //Connection failed 
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private void requestHandler(Request request) throws IOException {
        switch (request.getType()) {
            case LOGIN:
                loginHandler(request);
                break;
            case CHAT:
//              chatHandler(request);
                break;
            case SEND_MOVE:
                gameHandler(request);
                break;
            case SEND_INVITATION:
                invite(request);
                break;
            case SEND_REPLY:
                reply(request);
                break;
            case ACCEPT_INVITATION:
                acceptInvitation(request);
                break;
            case SEND_MSG:
                chatHandler(request);
                break;
            case END_GAME:
                playerTwo = null;

        }
    }

    public void loginHandler(Request loginRequest) throws IOException {
        //Authentication 
        onlinePlayer = new Player(recievingStream, sendingStream, loginRequest.getData("name"), "online");
        Server.onlinePlayers.add(onlinePlayer);
    }

    public void invite(Request playerTwoData) { //ClientSendPlayerData //derver will handle it 
        String requestedPlayer = playerTwoData.getData("destination");
        Server.onlinePlayers.forEach(player -> {
            if (requestedPlayer.equals(player.playerName)) {
                try {
                    Request invitation = new Request(RequestType.RECEIVE_INVITATION);
                    invitation.setData("source", onlinePlayer.playerName);
                    player.outputStream.writeObject(invitation);
                } catch (IOException ex) {
                    Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
                }
//               p2RecievingStream = new DataInputStream(playerTwo.playerSocket.getInputStream());
//               p2RecievingStream = new DataInputStream(playerTwo.playerSocket.getInputStream());
            }
        });
    }

    public void reply(Request replyData) {
        String destinationName = replyData.getData("destination");
        String replyResult = replyData.getData("reply");
        Server.onlinePlayers.forEach(player -> {
            if (destinationName.equals(player.playerName)) {
                try {
                    Request reply = new Request(RequestType.RECEIVE_REPLY);
                    reply.setData("source", onlinePlayer.playerName);
                    reply.setData("reply", replyResult);
                    player.outputStream.writeObject(reply);
                } catch (IOException ex) {
                    Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
    }

    private void gameHandler(Request request) throws IOException {
        String x = request.getData("x");
        String y = request.getData("y");
        Request game = new Request(RequestType.RECEIVE_MOVE);
        game.setData("x", x);
        game.setData("y", y);
        playerTwo.outputStream.writeObject(game);
    }

    private void chatHandler(Request request) throws IOException {
        System.out.println(onlinePlayer.playerName + " is sending msg to " + playerTwo.playerName );
        String msg = request.getData("msg");

        Request chatMsg = new Request(RequestType.RECEIVE_MSG);
        chatMsg.setData("msg", msg);
        playerTwo.outputStream.writeObject(chatMsg);
    }

    private void acceptInvitation(Request q) {
        String playerTwoAccepted = q.getData("destination");
        Server.onlinePlayers.forEach(player -> {
            if (playerTwoAccepted.equals(player.playerName)) {
                System.out.println("I am " + onlinePlayer.playerName + " and i have found " + player.playerName);
                playerTwo = player;
            }
        });
    }

}

