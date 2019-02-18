package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import db.DB;
import db.UserAccount;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.assets.Request;
import server.assets.RequestType;
import signInSignUp.ClientApp;

public class ServerSession extends Thread {

    Player onlinePlayer;
    Player playerTwo;
    Socket playerSocket;
    ObjectInputStream recievingStream;
    ObjectOutputStream sendingStream;
    Request request;
    DB database;
    Game loadedGame;
    int countX = 0;
    int countO = 0;
    ArrayList<String> offlinePlayers;
    //In this constructor i create recieveing from player
    public ServerSession(Socket ps, ObjectOutputStream sendingStream) throws IOException {
        playerSocket = ps;
        recievingStream = new ObjectInputStream(ps.getInputStream());
        this.sendingStream = sendingStream;
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
            } catch (SQLException ex) {
                Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void requestHandler(Request request) throws IOException, SQLException, InterruptedException {
        switch (request.getType()) {
            case SIGNUP:
                try {
                    signUpHandler(request);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case LOGIN:
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
            case END_GAME: //End the game while playing
                endGame();
                break;
            case QUIT_GAME:
                quitGame();
                break;
            case WIN:
                hundleWinner();
                break;
            case SAVE_GAME:
                saveGame(request.getGameData("game"));
                break;
            case CHECK_GAME:
                loadedGame = checkSavedGame();
                countX = 0;
                countO = 0;
                //Check the one who has the turn 
                if (loadedGame != null) {
                    checkPlayer(loadedGame.cell11);
                    checkPlayer(loadedGame.cell12);
                    checkPlayer(loadedGame.cell13);
                    checkPlayer(loadedGame.cell21);
                    checkPlayer(loadedGame.cell22);
                    checkPlayer(loadedGame.cell23);
                    checkPlayer(loadedGame.cell31);
                    checkPlayer(loadedGame.cell32);
                    checkPlayer(loadedGame.cell33);
                    if (countX == countO) {
                        if (loadedGame.player1.equals(onlinePlayer.playerName)) {
                            request = new Request(RequestType.PLAYER_X);
                            onlinePlayer.outputStream.writeObject(request);
                            request = new Request(RequestType.PLAYER_O);
                            playerTwo.outputStream.writeObject(request);
                        } else {
                            request = new Request(RequestType.PLAYER_O);
                            onlinePlayer.outputStream.writeObject(request);
                            request = new Request(RequestType.PLAYER_X);
                            playerTwo.outputStream.writeObject(request);
                        }
                    } else if (countX > countO) {
                        if (loadedGame.player1.equals(onlinePlayer.playerName)) {
                            request = new Request(RequestType.PLAYER_O);
                            onlinePlayer.outputStream.writeObject(request);
                            request = new Request(RequestType.PLAYER_X);
                            playerTwo.outputStream.writeObject(request);
                        } else {
                            request = new Request(RequestType.PLAYER_X);
                            onlinePlayer.outputStream.writeObject(request);
                            request = new Request(RequestType.PLAYER_O);
                            playerTwo.outputStream.writeObject(request);
                        }
                    }
                    request = new Request(RequestType.SAVED_GAME);
                    request.setSaveddGame("game", loadedGame);
                    sendingStream.writeObject(request);
                    playerTwo.outputStream.writeObject(request);
                }
                break;
        }
    }
    private void checkPlayer(String player) {
        if ("X".equals(player)) {
            countX++;
        } else if ("O".equals(player)) {
            countO++;
        }
    }
    private void hundleWinner() throws IOException {
        try {
            database.update(onlinePlayer.playerName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request = new Request(RequestType.LOSE);
        playerTwo.outputStream.writeObject(request);
    }
    public void signUpHandler(Request signUpRequest) throws IOException, SQLException {
        String user_name = signUpRequest.getData("username");
        String user_pass = signUpRequest.getData("pass");
        ArrayList<db.PlayerDB> players = database.getAll();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).username.equals(user_name)) {
                request = new Request(RequestType.REPEATED_USER);
                sendingStream.writeObject(request);
                return;
            }
        }
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
            ArrayList<db.PlayerDB> players = database.getAll();
            boolean flag = false;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).username.equals(user_name)
                        && players.get(i).pass.equals(user_pass)) {
                    flag = true;
                    break;
                }
            }
            boolean repeatedUser = false;
            for (int i = 0; i < Server.onlinePlayers.size(); i++) {
                if (Server.onlinePlayers.get(i).playerName.equals(user_name)) {
                    request = new Request(RequestType.REPEATED_LOGIN);
                    try {
                        repeatedUser = true;
                        sendingStream.writeObject(request);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (!repeatedUser) {
                if (flag) {
                    onlinePlayer = new Player(recievingStream, sendingStream, loginRequest.getData("username"), "online");
                    Server.onlinePlayers.add(onlinePlayer);
                    Request loginSuccessRequest = new Request(RequestType.LOGIN_SUCCESS);
                    sendingStream.writeObject(loginSuccessRequest);
                } else {
                    Request loginSuccessRequest = new Request(RequestType.LOGIN_FAILED);
                    sendingStream.writeObject(loginSuccessRequest);
                }
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
    private void gameHandler(Request request) {
        String x = request.getData("x");
        String y = request.getData("y");
        String playable = request.getData("current_player");
        Request game = new Request(RequestType.RECEIVE_MOVE);
        game.setData("x", x);
        game.setData("y", y);
        game.setData("current_player", playable);
        try {
            playerTwo.outputStream.writeObject(game);
        } catch (Exception e) {
            request = new Request(RequestType.CONNECTION_LOST);
            try {
                sendingStream.writeObject(request);
            } catch (IOException ex) {
                Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void chatHandler(Request request) {
        try {
            String msg = request.getData("msg");
            Request chatMsg = new Request(RequestType.RECEIVE_MSG);
            chatMsg.setData("msg", msg);
            playerTwo.outputStream.writeObject(chatMsg);
        } catch (IOException e) {
            request = new Request(RequestType.CONNECTION_LOST);
            try {
                sendingStream.writeObject(request);
            } catch (IOException ex) {
                Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void acceptInvitation(Request q) {
        String playerTwoAccepted = q.getData("destination");
        Server.onlinePlayers.forEach(player -> {
            if (playerTwoAccepted.equals(player.playerName)) {
                playerTwo = player;
                try {
                    sendBusyPlayer(player.playerName); //Send to all players that this player is busy
                } catch (IOException ex) {
                    Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
                }
                player.setSign('b'); // Set his sign to be busy
            }
        });
    }

    private void handleMultiRequest() throws IOException, SQLException {
        onlinePlayer.setSign('d');
        sendOnlinePlayers();
        sendOfflinePlayers();
    }
    private void closeConnection() throws IOException, SQLException {
        // if a player close the window
        if (onlinePlayer != null) {
            Server.onlinePlayers.remove(onlinePlayer);
            sendBusyPlayer(onlinePlayer.playerName);
            sendOnlinePlayers();
            sendOfflinePlayers();
        }
        playerSocket.close();
    }
    public void disconnectServer() throws IOException {
        request = new Request(RequestType.SERVER_DISCONNECTED);
        sendingStream.writeObject(request);
    }
    private void endGame() throws IOException, SQLException { //player disconnected
        request = new Request(RequestType.CONNECTION_LOST);
        playerTwo.outputStream.writeObject(request);
        playerTwo = null;
    }
    private void quitGame() throws IOException, SQLException { //player quit
        request = new Request(RequestType.QUIT_GAME);
        playerTwo.outputStream.writeObject(request);
        playerTwo = null;
    }
    public void sendBusyPlayer(String name) throws IOException {
        request = new Request(RequestType.BUSY_PLAYER);
        request.setData("busy", name);
        for (int i = 0; i < Server.onlinePlayers.size(); i++) {
            if (Server.onlinePlayers.get(i).getSign() == 'd') {
                Server.onlinePlayers.get(i).outputStream.writeObject(request);
            }
        }
    }
    private void saveGame(String[] gameArr) throws SQLException {
        database.insertNewGame(onlinePlayer.playerName, playerTwo.playerName,
                gameArr[0], gameArr[1], gameArr[2],
                gameArr[3], gameArr[4], gameArr[5],
                gameArr[6], gameArr[7], gameArr[8]);
    }
    private Game checkSavedGame() throws SQLException {
        ArrayList<Game> games = database.getGames();
        for (int i = 0; i < games.size(); i++) {
            if ((games.get(i).player1.equals(onlinePlayer.playerName) && games.get(i).player2.equals(playerTwo.playerName))) {
                database.removeSavedGame(onlinePlayer.playerName, playerTwo.playerName);
                return games.get(i);
            }
        }
        return null;
    }
    public ArrayList<String> getOffline() throws SQLException {
        ArrayList<String> allPlayers = new ArrayList<>();
        ArrayList<String> onlinePlayers = new ArrayList<>();
        ArrayList<db.PlayerDB> players = new DB().getAll();
        for (int i = 0; i < players.size(); i++) {
            allPlayers.add(players.get(i).username);
        }
        for (int i = 0; i < Server.onlinePlayers.size(); i++) {
            onlinePlayers.add(Server.onlinePlayers.get(i).playerName);
        }
        allPlayers.removeAll(onlinePlayers);
        return allPlayers;
    }
    public void sendOfflinePlayers() throws SQLException, IOException {
        offlinePlayers = getOffline();
        request = new Request(RequestType.OFFLINE_PLAYERS);
        request.set_online_Data("offlinePlayers", offlinePlayers);
        for (int i = 0; i < Server.onlinePlayers.size(); i++) {
            if (Server.onlinePlayers.get(i).getSign() == 'd') {
                Server.onlinePlayers.get(i).outputStream.writeObject(request);
            }
        }
    }
        public void sendOnlinePlayers() throws IOException {
        request = new Request(RequestType.ONLINE_PLAYERS);
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < Server.onlinePlayers.size(); i++) {
            if (Server.onlinePlayers.get(i).getSign() == 'd') {
                arr.add(Server.onlinePlayers.get(i).playerName);
            }
        }
        request.set_online_Data("online_players", arr);
        for (int i = 0; i < Server.onlinePlayers.size(); i++) {
            if (Server.onlinePlayers.get(i).getSign() == 'd') {
                Server.onlinePlayers.get(i).outputStream.writeObject(request);
            }
        }
    }
}
