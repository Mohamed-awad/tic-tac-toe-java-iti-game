package db;

import db.PlayerDB;
import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.ArrayList;
import server.Game;

public class DB {

    int affectedRow;
    PreparedStatement pStatement;
    Connection con;
    ResultSet rs;
    ArrayList<PlayerDB> player_list;
    public DB() {
        try {
            DriverManager.registerDriver(new Driver());
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicTacToe",
                    "motaz", "1234");
            player_list = new ArrayList<>();
        } catch (SQLException ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
    //return all players
    public ArrayList<PlayerDB> getAll() throws SQLException {
        Statement stmt = con.createStatement();
        String queryString = "select * from players";
        ResultSet rs = stmt.executeQuery(queryString);
        while (rs.next()) {
            player_list.add(new PlayerDB(rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getInt(5)));
        }
        return player_list;
    }
    //update the player score
    public void update(String username) throws SQLException {
        pStatement = con.prepareStatement("UPDATE players SET score = score + 10 WHERE username = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        pStatement.setString(1, username);
        affectedRow = pStatement.executeUpdate();
    }
    //insert new player
    public void insert(String username, String pass, String status, int score) throws SQLException {
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
    //insert saved game
    public void insertNewGame(String player1, String player2, String cell11, String cell12, String cell13,
            String cell21, String cell22, String cell23, String cell31, String cell32, String cell33) throws SQLException {
        pStatement = con.prepareStatement("INSERT into games (player1,player2,cell11,"
                + "cell12,cell13,cell21,cell22,cell23,cell31,"
                + "cell32,cell33) VALUES (?,?,?,?,?,?,?,?,?,?,?);",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        pStatement.setString(1, player1);
        pStatement.setString(2, player2);
        pStatement.setString(3, cell11);
        pStatement.setString(4, cell12);
        pStatement.setString(5, cell13);
        pStatement.setString(6, cell21);
        pStatement.setString(7, cell22);
        pStatement.setString(8, cell23);
        pStatement.setString(9, cell31);
        pStatement.setString(10, cell32);
        pStatement.setString(11, cell33);
        pStatement.executeUpdate();
    }
    //get all games to search for a saved game
    public ArrayList<Game> getGames() throws SQLException {
        ArrayList<Game> games = new ArrayList<>();
        Statement stmt = con.createStatement();
        String queryString = "select player1,player2,cell11,cell12,cell13,cell21,cell22,cell23,cell31,cell32,cell33 from games";
        ResultSet rs = stmt.executeQuery(queryString);
        while (rs.next()) {
            System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3)
                    + rs.getString(4) + rs.getString(5) + rs.getString(6) + rs.getString(7)
                    + rs.getString(8) + rs.getString(9) + rs.getString(10) + rs.getString(11));
            games.add(new Game(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                     rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11)));
        }
        return games;
    }
    //Remove saved Game After loading it 
    public void removeSavedGame(String p1, String p2) throws SQLException {
        pStatement = con.prepareStatement("DELETE FROM games WHERE (player1 = ? and player2 = ? ) or (player1=? and player2=?);",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        pStatement.setString(1, p1);
        pStatement.setString(2, p2);
        pStatement.setString(3, p2);
        pStatement.setString(4, p1);

        affectedRow = pStatement.executeUpdate();
    }
}
