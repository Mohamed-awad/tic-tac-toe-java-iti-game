package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import server.Game;
import server.assets.Request;
import server.assets.RequestType;
import signInSignUp.ClientApp;

public class ClientSession extends Thread {

    //CreateObject of gui
    Socket serverSocket;
    ObjectInputStream recievingStream;
    ObjectOutputStream sendingStream;
    Request request;
    String response;
    ArrayList<String> online_players;
    ArrayList<String> players_invite_me;
    String source;
    String myName;
    String emptyArr[] = {"", "", "", "", "", "", "", "", ""};
    public ClientSession(Socket playerSocket) throws IOException {
        response = "";
        this.serverSocket = playerSocket;
        sendingStream = new ObjectOutputStream(playerSocket.getOutputStream());
        recievingStream = new ObjectInputStream(playerSocket.getInputStream());
        setDaemon(true);
        start();
        players_invite_me = new ArrayList<>();
    }
    public void run() {
        while (true) {
            try {
                request = (Request) recievingStream.readObject();
                requestHandler(request);
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }
    public void requestHandler(Request request) throws IOException {
        switch (request.getType()) {
            case SIGN_UP_SUCCESS:
                response = "success";
                break;
            case LOGIN_SUCCESS:
                response = "success";
                break;
            case LOGIN_FAILED:
                response = "failed";
                break;
            case ONLINE_PLAYERS:
                handleOnlinePlayers(request);
                break;
            case OFFLINE_PLAYERS:
                System.out.println("offlineplayersReceived");
                handleOfflinePlayers(request);
                break;
            case RECEIVE_INVITATION:
                handleInvitation(request);
                break;
            case LOSE:
                LoseHandler();
                break;
            case RECEIVE_REPLY:
                handleReply(request);
                break;
            case RECEIVE_MOVE:
                handleMove(request);
                break;
            case RECEIVE_MSG:
                handleMsg(request);
                break;
            case CONNECTION_LOST:
                saveGameToServer();
                Platform.runLater(() -> {
                    ClientApp.connectionError();
                });
                break;
            case SERVER_DISCONNECTED:
                saveGameToServer();
                Platform.runLater(() -> {
                    ClientApp.disconnectServer();
                });
                break;
            case BUSY_PLAYER: //remove the player from the lists
                Platform.runLater(() -> {
                    ClientApp.removeBusyPlayer(request.getData("busy"));
                });
                break;
            case QUIT_GAME:
                saveGameToServer();
                Platform.runLater(() -> {
                    ClientApp.connectionError();
                });
                break;
            case REPEATED_LOGIN:
                Platform.runLater(() -> {
                    ClientApp.repeated("You are already logged in", "signin");
                });
                break;
            case REPEATED_USER:
                Platform.runLater(() -> {
                    ClientApp.repeated("You are already registered", "signup");
                });
                break;
            case SAVED_GAME:
                Platform.runLater(() -> {
                    ClientApp.showSavedGameExist();
                    loadGame(request);
                });
                break;
            case PLAYER_X:
                ClientApp.game.playable = true;
                ClientApp.game.your_turn = true;
                break;
            case PLAYER_O:
                ClientApp.game.your_turn = true;
                ClientApp.game.playable = false;
                break;
            case TIE:
            	hundle_tie();
            	break;
        }
    }
    
    private void hundle_tie() {
    	Platform.runLater(() -> {
    		ClientApp.tie();
    	});
	}
	public void sendTie() throws IOException  {
    	Request tie = new Request(RequestType.TIE);
        sendingStream.writeObject(tie);
    }
    
    public void LoseHandler() {
        Platform.runLater(() -> {
            try {
                ClientApp.alert_loser();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
    // send sign up Request
    public void signup(String loginName, String Pass) throws IOException {
        Request signUpRequest = new Request(RequestType.SIGNUP);
        signUpRequest.setData("username", loginName);
        signUpRequest.setData("pass", Pass);
        sendingStream.writeObject(signUpRequest);
    }
    public String return_response() {
        return response;
    }
    public void login(String loginName, String Pass) throws IOException {
        source = loginName;
        Request loginRequest = new Request(RequestType.LOGIN);
        loginRequest.setData("username", loginName);
        loginRequest.setData("pass", Pass);
        sendingStream.writeObject(loginRequest);
    }
    public void handleOnlinePlayers(Request online_request) {
        online_players = online_request.get_online_Data("online_players");
        Platform.runLater(() -> {
            ClientApp.multiMain.sendIvitationObservableList.clear();
            for (int i = 0; i < online_players.size(); i++) {
                if (!online_players.get(i).equals(source)) {
                    ClientApp.multiMain.sendIvitationObservableList.add(online_players.get(i));
                }
            }
        });
    }
        private void handleOfflinePlayers(Request request) {
        ArrayList<String> players = request.get_online_Data("offlinePlayers");
        Platform.runLater(() -> {
            ClientApp.multiMain.OFFlinePeople.clear();
            for (int i = 0; i < players.size(); i++) {
                ClientApp.multiMain.OFFlinePeople.add(players.get(i));
            }
        });
    }
    public ArrayList<String> return_online_players() {
        return online_players;
    }
    public void sendInvitation(String name) throws IOException {
        Request inviteRequest = new Request(RequestType.SEND_INVITATION);
        inviteRequest.setData("source", source);
        inviteRequest.setData("destination", name);
        sendingStream.writeObject(inviteRequest);
    }
    public void handleInvitation(Request request) {
        String sourceOfInv = request.getData("source");
        Platform.runLater(() -> {
            ClientApp.multiMain.AcceptInvitationObserveList.clear();
            ClientApp.multiMain.AcceptInvitationObserveList.add(sourceOfInv);
        });
    }
    public void sendReply(String playerName, String replyResult) throws IOException {
        Request reply = new Request(RequestType.SEND_REPLY);
        if ("accept".equals(replyResult)) {
            acceptInvitation(playerName, true, true);
        }
        reply.setData("reply", replyResult);
        reply.setData("destination", playerName);
        sendingStream.writeObject(reply);
    }
    public void handleReply(Request request) throws IOException {
        String result = request.getData("reply");
        String player = request.getData("source");
        if ("accept".equals(result)) {
            acceptInvitation(player, false, true);
        }
    }
    public void acceptInvitation(String player, Boolean x, Boolean y) throws IOException {
        ClientApp.gameArr = emptyArr.clone();
        Request r = new Request(RequestType.ACCEPT_INVITATION);
        r.setData("destination", player);
        Platform.runLater(() -> {
            ClientApp.game = new TicTacGame(x, y);
            try {
                ClientApp.game.start(ClientApp.mainStage);
                sendingStream.writeObject(r);
                request = new Request(RequestType.CHECK_GAME);
                ClientApp.sessionHandler.sendingStream.writeObject(request);
            } catch (IOException ex) {
                Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    public void endConnection() throws IOException {
        request = new Request(RequestType.END_SESSION);
        sendingStream.writeObject(request);
    }
    //Handling gaming
    public void sendMove(int x, int y, String playable) throws IOException {
        Request move = new Request(RequestType.SEND_MOVE);
        move.setData("x", Integer.toString(x));
        move.setData("y", Integer.toString(y));
        move.setData("current_player", playable);
        saveIntoArr(x, y, playable);
        sendingStream.writeObject(move);
    }
    private void handleMove(Request request) {
        int x = Integer.valueOf(request.getData("x"));
        int y = Integer.valueOf(request.getData("y"));
        String playable = request.getData("current_player");
        saveIntoArr(x, y, playable);
        Platform.runLater(() -> {
            ClientApp.game.setMove(x, y, playable);
        });
    }
//    Handle Chat     
    public void sendMsg(String msg) throws IOException {
        Request chatMsg = new Request(RequestType.SEND_MSG);
        chatMsg.setData("msg", msg);
        sendingStream.writeObject(chatMsg);
    }
    private void handleMsg(Request request) {
        String msg = request.getData("msg");
        Platform.runLater(() -> {
            ClientApp.game.setMsg(msg);
        });
    }
    public void quitGame() throws IOException {
        request = new Request(RequestType.QUIT_GAME);
        sendingStream.writeObject(request);
    }
    public void sendWin() throws IOException {
        request = new Request(RequestType.WIN);
        sendingStream.writeObject(request);
    }
//
//    public void endGame() throws IOException {
//        Request endRequest = new Request(RequestType.END_GAME);
//        sendingStream.writeObject(endRequest);
//    }
    public void startMultiGame() throws IOException {
        request = new Request(RequestType.MULTI_GAME);
        sendingStream.writeObject(request);
    }
    private void saveIntoArr(int xx, int yy, String player) {
        int pos = 0;
        switch (xx) {
            case 0:
                switch (yy) {
                    case 0:
                        pos = 0;
                        break;
                    case 1:
                        pos = 1;
                        break;
                    case 2:
                        pos = 2;
                        break;
                }
                break;
            case 1:
                switch (yy) {
                    case 0:
                        pos = 3;
                        break;
                    case 1:
                        pos = 4;
                        break;
                    case 2:
                        pos = 5;
                        break;
                }
                break;
            case 2:
                switch (yy) {
                    case 0:
                        pos = 6;
                        break;
                    case 1:
                        pos = 7;
                        break;
                    case 2:
                        pos = 8;
                        break;
                }
                break;
        }
        ClientApp.gameArr[pos] = player;
    }
    public void saveGameToServer() throws IOException {
        request = new Request(RequestType.SAVE_GAME);
        request.setGameData("game", ClientApp.gameArr);
        sendingStream.writeObject(request);
    }
    private void loadGame(Request request) {
        Game loadedGame = request.getSavedGame("game");
        String returnedArr[] = {"", "", "", "", "", "", "", "", ""};
        returnedArr[0] = loadedGame.cell11;
        returnedArr[1] = loadedGame.cell12;
        returnedArr[2] = loadedGame.cell13;
        returnedArr[3] = loadedGame.cell21;
        returnedArr[4] = loadedGame.cell22;
        returnedArr[5] = loadedGame.cell23;
        returnedArr[6] = loadedGame.cell31;
        returnedArr[7] = loadedGame.cell32;
        returnedArr[8] = loadedGame.cell33;
        ClientApp.gameArr = returnedArr.clone();
        if (!"".equals(loadedGame.cell11)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(0, 0, loadedGame.cell11);
            });
        }
        if (!"".equals(loadedGame.cell12)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(0, 1, loadedGame.cell12);
            });
        }
        if (!"".equals(loadedGame.cell13)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(0, 2, loadedGame.cell13);
            });
        }
        if (!"".equals(loadedGame.cell21)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(1, 0, loadedGame.cell21);
            });
        }
        if (!"".equals(loadedGame.cell22)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(1, 1, loadedGame.cell22);
            });
        }
        if (!"".equals(loadedGame.cell23)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(1, 2, loadedGame.cell23);
            });
        }
        if (!"".equals(loadedGame.cell31)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(2, 0, loadedGame.cell31);
            });
        }
        if (!"".equals(loadedGame.cell32)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(2, 1, loadedGame.cell32);
            });
        }
        if (!"".equals(loadedGame.cell33)) {
            Platform.runLater(() -> {
                ClientApp.game.setMove(2, 2, loadedGame.cell33);
            });
        }
    }

}
