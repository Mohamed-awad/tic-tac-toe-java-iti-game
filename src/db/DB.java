package db;

import db.Player;

import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.ArrayList;

public class DB {

    int affectedRow;
    PreparedStatement pStatement;
    Connection con;
    ResultSet rs;
    ArrayList<Player> player_list;

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

    public ArrayList<Player> getAll() throws SQLException {
        Statement stmt = con.createStatement();
        String queryString = new String("select * from players");
        ResultSet rs = stmt.executeQuery(queryString);
        while (rs.next()) {
            player_list.add(new Player(rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getInt(5)));
        }
        return player_list;
    }

    public void update(int id, String username, String pass, String status, int score) throws SQLException {
        pStatement = con.prepareStatement("UPDATE players SET username  = ? ,"
                + " pass  = ?,status = ?,score = ? WHERE ID = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        pStatement.setString(1, username);
        pStatement.setString(2, pass);
        pStatement.setString(3, status);
        pStatement.setInt(4, score);
        pStatement.setInt(5, id);

        affectedRow = pStatement.executeUpdate();
    }

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
}
