package server.assets;

import java.io.Serializable;
import java.util.HashMap;

public class Request implements Serializable{
	private RequestType type;
	public HashMap<String, String> requestData;
            
    public Request(RequestType type){
        this.type = type;
        requestData = new HashMap<>();
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
}
