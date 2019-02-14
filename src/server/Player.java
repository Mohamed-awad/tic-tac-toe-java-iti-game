package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
	
	public void setSign (char c){
		sign = c;
	}
        public char getSign(){
            return sign;
        }
}
