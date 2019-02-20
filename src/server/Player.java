package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//this class is used to save streams to be used in server
public class Player {

	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;
	public String playerName;
	String playerState;
	char sign;

	Player(ObjectInputStream i, ObjectOutputStream o, String name, String state) {
		playerName = name;
		inputStream = i;
		outputStream = o;
		playerState = state; //Idle or Playing
	}
	//this for setting the status
	public void setSign (char c){
		sign = c;
	}
        public char getSign(){
            return sign;
        }
}
