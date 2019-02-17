package server;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.application.Application.launch;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import signInSignUp.Sign_up;
import db.DB;
import db.Player;
import db.UserAccount;

public class Server_Mode extends Application {

    Server myServer;
    @Override
    public void start(Stage primaryStage) throws Exception {
        myServer = new Server();
        
        TableView<UserAccount> table = new TableView<UserAccount>();
        
        // Create column UserName (Data type of String).
        TableColumn<UserAccount, String> userNameCol //
                = new TableColumn<UserAccount, String>("User Name");
   
        // Active Column
        TableColumn<UserAccount, Boolean> activeCol//
                = new TableColumn<UserAccount, Boolean>("Active");
   
        // Defines how to fill data for each cell.
        // Get value from property of UserAccount. .
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userNameCol.setId("UserCol");
   
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
      
        // Set Sort type for userName column
        userNameCol.setSortType(TableColumn.SortType.DESCENDING);
        
   
        // Display row data
        ObservableList<UserAccount> list = getUserList();
        table.setItems(list);
   
        table.getColumns().addAll(userNameCol, activeCol);
   
        GridPane root = new GridPane();
        root.setPadding(new Insets(5));
        root.add(table , 0 , 0 , 3 , 20);
        
        Button Btn_Start = new Button();
        Btn_Start.setText("Start Server");
        Btn_Start.setId("loginbtn");
        
        Button Btn_Stop = new Button();
        Btn_Stop.setText("Stop Server");
        Btn_Stop.setId("loginbtn");
  
        // add actions on buttons
        Btn_Start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myServer.startServer();
                root.getChildren().remove(Btn_Start);
                root.add(Btn_Stop, 0, 7, 3, 40);
            }
        });
        
        Btn_Stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myServer.stopServer();
                root.getChildren().remove(Btn_Stop);
                root.add(Btn_Start, 0, 7, 3, 40);
            }
        });
        

        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(0, 10, 0, 10));
        root.add(Btn_Start , 0, 7 , 3 , 40);
        
        
        root.setAlignment(Pos.CENTER);
        Btn_Start.setMaxWidth(Double.MAX_VALUE);
        Btn_Stop.setMaxWidth(Double.MAX_VALUE);
        
        Scene scene = new Scene(root, 400, 500);   
        scene.getStylesheets().add(Sign_up.class.getResource("GameStyle.css").toExternalForm());
        
        primaryStage.setTitle("Server Status");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
    }
    
    private ObservableList<UserAccount> getUserList() throws SQLException {
    	ArrayList<Player> players = new DB().getAll();
    	ObservableList<UserAccount> list = FXCollections.observableArrayList();
    	for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			UserAccount user1 = new UserAccount(i+1L, p.username , p.score);
			list.add(user1);
		}  
        return list;
    }
    
    public void stop() throws IOException {
        myServer.stopServer();
        Platform.exit();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
