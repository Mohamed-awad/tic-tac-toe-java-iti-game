package server.assets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import server.Player;

public class Request implements Serializable{
	private RequestType type;
	public HashMap<String, String> requestData;
	public HashMap<String, ArrayList<String>> onlineData;
            
    public Request(RequestType type){
        this.type = type;
        requestData = new HashMap<>();
        onlineData = new HashMap<>();
    }
    public void setType(RequestType type){
        this.type = type;
    }
    public RequestType getType(){ 
        return type; 
    }
    public void setData(String key, String value){
        requestData.put(key,value);
    }
    public String getData(String key){
        if(requestData.containsKey(key))
            return requestData.get(key);
        else
            return null;
    }
    
    public void set_online_Data(String key, ArrayList<String> value){
        onlineData.put(key, value);
    }
    public ArrayList<String> get_online_Data(String key){
        if(onlineData.containsKey(key))
            return onlineData.get(key);
        else
            return null;
    }
}
