package db;

public class PlayerDB {

    public int id, score;
    public String username, pass, status;
    
    public PlayerDB(int id, String username, String pass, String status, int score)
    {
        this.id = id;
        this.username = username;
        this.pass = pass;
        this.status = status;
        this.score = score;
    }
    public String getUserName()
    {
    	return this.username;
    }
    public int getScore()
    {
    	return this.score;
    }
}
