package server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.application.Application.launch;
import java.io.IOException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import signInSignUp.Sign_up;
import db.Player;

public class Server_Mode extends Application {

    Server myServer;
    @Override
    public void start(Stage primaryStage) throws Exception {
        myServer = new Server();
        
        TableView<Player> table = new TableView<Player>();
        
        // Create column UserName (Data type of String).
        TableColumn<Player, String> userNameCol //
                = new TableColumn<Player, String>("UserName");
   
        // Active Column
        TableColumn<Player, Integer> activeCol//
                = new TableColumn<Player, Integer>("Score");
   
        // Defines how to fill data for each cell.
        // Get value from property of UserAccount. .
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        userNameCol.setId("UserCol");
   
        activeCol.setCellValueFactory(new PropertyValueFactory<>("score"));
      
        // Set Sort type for userName column
        userNameCol.setSortType(TableColumn.SortType.DESCENDING);
        
   
        // Display row data
        ObservableList<Player> list = getUserList();
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
    
    private ObservableList<Player> getUserList() {
    	// get all players from database
    	
    	ObservableList<Player> list = FXCollections.observableArrayList();
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
