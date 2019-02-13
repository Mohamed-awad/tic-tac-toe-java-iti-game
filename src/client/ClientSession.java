package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Player;
import server.assets.Request;
import server.assets.RequestType;

public class ClientSession extends Thread {

    //CreateObject of gui
    Socket serverSocket;
    ObjectInputStream recievingStream;
    ObjectOutputStream sendingStream;
    Request request;
    Boolean response;

    public ClientSession(Socket playerSocket) throws IOException {
    	response = false;
        this.serverSocket = playerSocket;
        sendingStream = new ObjectOutputStream(playerSocket.getOutputStream());
        recievingStream = new ObjectInputStream(playerSocket.getInputStream());
        start();
    }

    public void run() {
        while (true) {
            try {
                System.out.println("next2");
                request = (Request) recievingStream.readObject();
                requestHandler(request);
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (ClassNotFoundException ex) {
                System.out.println("ddsad");
                Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void requestHandler(Request request) throws IOException {
        switch (request.getType()) {
        	case SIGN_UP_SUCCESS:
        		response = true;
        		break;
        	case LOGIN_SUCCESS:
        		response = true;
        		break;
        	case LOGIN_FAILED:
        		response = false;
        		break;
//            case CHAT:
//             chatHandler(request);
//                break;
//            case RECEIVE_INVITATION:
//                handleInvitation(request);
//                break;
//            case RECEIVE_REPLY:
//                handleReply(request);
//                break;
//            case RECEIVE_MOVE:
//                handleMove(request);
//                break;
//            case RECEIVE_MSG:
//                handleMsg(request);
//                break;
        }
    }

    // send sign up Request
    public void signup(String loginName, String Pass) throws IOException {
    	Request signUpRequest = new Request(RequestType.SIGNUP);
    	signUpRequest.setData("username", loginName);
    	signUpRequest.setData("pass", Pass);
        sendingStream.writeObject(signUpRequest);
    }
    
    public boolean return_response()
    {
    	return response;
    }
    
    public void login(String loginName, String Pass) throws IOException {
        Request loginRequest = new Request(RequestType.LOGIN);
        loginRequest.setData("username", loginName);
        loginRequest.setData("pass", Pass);
        sendingStream.writeObject(loginRequest);
    }
//    
//    public void handleInvitation(Request request) {
//        String source = request.getData("source");
//        //Show it in gui
//    }
//
//    public void sendInvitation(String playerName) throws IOException {
//        Request invitation = new Request(RequestType.SEND_INVITATION);
//        invitation.setData("destination", playerName);
//        sendingStream.writeObject(invitation);
//    }
//
//    public void sendReply(String playerName, String replyResult) throws IOException {
//        Request reply = new Request(RequestType.SEND_REPLY);
//            if ("accept".equals(replyResult)) {
//            acceptInvitation(playerName);
//        }
//        reply.setData("reply", replyResult);
//        reply.setData("destination", playerName);
//        sendingStream.writeObject(reply);
//    }
//    
//    public void handleReply(Request request) throws IOException {
//        String result = request.getData("reply");
//        String player = request.getData("source");
//        if ("accept".equals(result)) {
//            acceptInvitation(player);
//        }
//
//    }
//    public void acceptInvitation (String player) throws IOException{
//        Request r= new Request(RequestType.ACCEPT_INVITATION);
//        r.setData("destination", player);
//        sendingStream.writeObject(r);
//    }
//
//    //Handling gaming
//    public void sendMove(int x, int y) throws IOException {
//        Request move = new Request(RequestType.SEND_MOVE);
//        move.setData("x", Integer.toString(x));
//        move.setData("y", Integer.toString(y));
//        sendingStream.writeObject(move);
//    }
//
//    private void handleMove(Request request) {
//        int x = Integer.valueOf(request.getData("x"));
//        int y = Integer.valueOf(request.getData("y"));
//        System.out.println("x is " + x + " y is " + y);
//        //Handle the gui
//
//    }
//
//    //Handle Chat 
//    //Handling gaming
//    public void sendMsg(String msg) throws IOException {
//        Request chatMsg = new Request(RequestType.SEND_MSG);
//        chatMsg.setData("msg", msg);
//        sendingStream.writeObject(chatMsg);
//    }
//
//    private void handleMsg(Request request) {
//        String msg = request.getData("msg");
//        System.out.println(msg);
//        //Handle the gui
//
//    }
//
//    public void endGame() throws IOException {
//        Request endRequest = new Request(RequestType.END_GAME);
//        sendingStream.writeObject(endRequest);
//    }
    
//    public static void main(String[] args) throws IOException, InterruptedException {
//        Socket serverSockett = new Socket("localhost", 5000);
//        ClientSession sessionHandler = new ClientSession(serverSockett);
//        sessionHandler.login("hesham", "123456789");
//        Thread.sleep(10000);
//        sessionHandler.sendReply("Hesham", "accept");
//        sessionHandler.sendMsg("message from motaz");
//        Thread.sleep(10000);
//    }

}
