package signInSignUp;
import signInSignUp.Player;
import com.mysql.jdbc.Driver ;
import java.sql.* ;
import java.util.ArrayList;

public class DB
{
    int affectedRow;
	PreparedStatement pStatement;
    Connection con;
    ResultSet rs;
    ArrayList <Player> player_list;
	public DB()
	{
		try
		{
			DriverManager.registerDriver(new Driver());
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicTacToe",
					"root","01111451253");
			player_list = new ArrayList<>();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
	}
 
	 public ArrayList<Player> getAll() throws SQLException{
		 Statement stmt = con.createStatement() ;
			String queryString = new String("select * from players");
			ResultSet rs = stmt.executeQuery(queryString) ;
			while(rs.next())
			{
				player_list.add(new Player(rs.getInt(1), rs.getString(2), 
						rs.getString(3), rs.getString(4), rs.getInt(5)));
			}
//			stmt.close();
//			con.close();
			return player_list;
	 }

	 public void update(int id , String username, String pass, String status, int score) throws SQLException{
		  pStatement = con.prepareStatement("UPDATE players SET username  = ? ,"
			+ " pass  = ?,status = ?,score = ? WHERE ID = ?;",
		  ResultSet.TYPE_SCROLL_INSENSITIVE,
		  ResultSet.CONCUR_UPDATABLE);    
		  pStatement.setString(1, username);
		  pStatement.setString(2, pass);
		  pStatement.setString(3, status);
		  pStatement.setInt(4, score);
		  pStatement.setInt(5,id);
		
		  affectedRow = pStatement.executeUpdate();
	 }
	 
     public void insert(String username, String pass, String status, int score) throws SQLException{
         
          pStatement = con.prepareStatement("INSERT INTO players (username, pass, status, score)"
          		+ " VALUES (? , ? ,? , ?);",
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_UPDATABLE);    
          pStatement.setString(1, username);
          pStatement.setString(2, pass);
          pStatement.setString(3, status);
          pStatement.setInt(4, score);
          pStatement.executeUpdate();
     }
	
//	public static void main(String args[]){
//		DB dd = new DB();
//		try {			
//			dd.insert("motaz", "mmmm", "1", 15);
//			ArrayList<Player> pp = dd.getAll();
//			System.out.println(pp.get(2).score + "  " + pp.get(2).username);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} 
//	}
}