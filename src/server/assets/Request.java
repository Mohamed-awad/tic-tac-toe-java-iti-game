package server.assets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import server.Game;
import server.Player;

public class Request implements Serializable {

    private RequestType type;
    public HashMap<String, String> requestData;
    public HashMap<String, ArrayList<String>> onlineData;
    public HashMap<String, String[]> gameData;
    public HashMap<String, Game> savedGame;
    public Request(RequestType type) {
        this.type = type;
        requestData = new HashMap<>();
        onlineData = new HashMap<>();
        gameData = new HashMap<>();
        savedGame = new HashMap<>();
    }
    public void setType(RequestType type) {
        this.type = type;
    }
    public RequestType getType() {
        return type;
    }
    public void setData(String key, String value) {
        requestData.put(key, value);
    }
    public String getData(String key) {
        if (requestData.containsKey(key)) {
            return requestData.get(key);
        } else {
            return null;
        }
    }
    public void set_online_Data(String key, ArrayList<String> value) {
        onlineData.put(key, value);
    }
    public ArrayList<String> get_online_Data(String key) {
        if (onlineData.containsKey(key)) {
            return onlineData.get(key);
        } else {
            return null;
        }
    }
    public void setGameData(String key, String[] value) {
        gameData.put(key, value);
    }
    public String[] getGameData(String key) {
        if (gameData.containsKey(key)) {
            return gameData.get(key);
        } else {
            return null;
        }
    }
    public void setSaveddGame(String key, Game value) {
        savedGame.put(key, value);
    }
    public Game getSavedGame(String key) {
        if (savedGame.containsKey(key)) {
            return savedGame.get(key);
        } else {
            return null;
        }
    }
}
