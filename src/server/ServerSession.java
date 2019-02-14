package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import db.DB;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
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
    DB database;
    //In this constructor i create recieveing from player
    public ServerSession(Socket ps) throws IOException {
        playerSocket = ps;
        recievingStream = new ObjectInputStream(ps.getInputStream());
        sendingStream = new ObjectOutputStream(ps.getOutputStream());
        database = new DB();
        setDaemon(true);
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
            case SIGNUP:
                signUpHandler(request);
                break;
            case LOGIN:
                System.out.println("handling login");
                loginHandler(request);
                break;
            case ONLINE_PLAYERS:
                get_online_players();
                break;
            case SEND_INVITATION:
                invite(request);
                break;
            case MULTI_GAME:
                handleMultiRequest();
                break;
            case CHAT:
                chatHandler(request);
                break;
            case SEND_MOVE:
                gameHandler(request);
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
            case END_SESSION:
                closeConnection();
                break;
//            case END_GAME:
//                playerTwo = null;
//                break;
        }
    }
    public void signUpHandler(Request signUpRequest) throws IOException {
        String user_name = signUpRequest.getData("username");
        String user_pass = signUpRequest.getData("pass");
        try {
            database.insert(user_name, user_pass, "1", 0);
            Request signUpSuccessRequest = new Request(RequestType.SIGN_UP_SUCCESS);
            sendingStream.writeObject(signUpSuccessRequest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void loginHandler(Request loginRequest) throws IOException {
        String user_name = loginRequest.getData("username");
        String user_pass = loginRequest.getData("pass");
        try {
            ArrayList<db.Player> players = database.getAll();
            boolean flag = false;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).username.equals(user_name)
                        && players.get(i).pass.equals(user_pass)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                onlinePlayer = new Player(recievingStream, sendingStream, loginRequest.getData("username"), "online");
                Server.onlinePlayers.add(onlinePlayer);
                Request loginSuccessRequest = new Request(RequestType.LOGIN_SUCCESS);
                System.out.println("before sending");
                sendingStream.writeObject(loginSuccessRequest);
                System.out.println("lets send");
            } else {
                Request loginSuccessRequest = new Request(RequestType.LOGIN_FAILED);
                sendingStream.writeObject(loginSuccessRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void get_online_players() throws IOException {
        Request onlineplayers_request = new Request(RequestType.ONLINE_PLAYERS);
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < Server.onlinePlayers.size(); i++) {
            arr.add(Server.onlinePlayers.get(i).playerName);
        }
        onlineplayers_request.set_online_Data("online_players", arr);
        sendingStream.writeObject(onlineplayers_request);
    }
    public void invite(Request playerTwoData) { //ClientSendPlayerData //derver will handle it 
        String requestedPlayer = playerTwoData.getData("destination");
        Server.onlinePlayers.forEach(player -> {
            if (requestedPlayer.equals(player.playerName)) {
                try {
                    Request invitation = new Request(RequestType.RECEIVE_INVITATION);
                    invitation.setData("source", playerTwoData.getData("source"));
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
        System.out.println(onlinePlayer.playerName + " is sending msg to " + playerTwo.playerName);
        String msg = request.getData("msg");
        Request chatMsg = new Request(RequestType.RECEIVE_MSG);
        chatMsg.setData("msg", msg);
        playerTwo.outputStream.writeObject(chatMsg);
    }
    private void acceptInvitation(Request q) {
        String playerTwoAccepted = q.getData("destination");
        Server.onlinePlayers.forEach(player -> {
            if (playerTwoAccepted.equals(player.playerName)) {
                playerTwo = player;
            }
        });
    }
    public void sendOnlinePlayers() throws IOException {
        request = new Request(RequestType.ONLINE_PLAYERS);
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < Server.onlinePlayers.size(); i++) {
            arr.add(Server.onlinePlayers.get(i).playerName);
        }
        request.set_online_Data("online_players", arr);
        for (int i = 0; i < Server.onlinePlayers.size(); i++) {
            if (Server.onlinePlayers.get(i).getSign() == 'd') {
                Server.onlinePlayers.get(i).outputStream.writeObject(request);
            }
        }
    }
    private void handleMultiRequest() throws IOException {
        onlinePlayer.setSign('d');
        sendOnlinePlayers();
    }
    private void closeConnection() throws IOException {
        Server.onlinePlayers.remove(onlinePlayer);
        sendOnlinePlayers();
                playerSocket.close();

    }
    //Send signals to all clients
    public void endConnection() throws IOException{
        playerSocket.close();
    }
    
}
