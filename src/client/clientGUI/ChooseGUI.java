package client.clientGUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import client.clientGUI.ClientApp;
import client.clientGUI.Sign_up;
//This class is for choosing between single and multiPlayer
public class ChooseGUI {

    GridPane grid = new GridPane();
    
    public void start(Stage primaryStage) throws Exception {

    	Button btn_log = new Button();
        btn_log.setText("Single Mode");
        btn_log.setId("loginbtn");
        
        Button btn_signup = new Button();
        btn_signup.setText("Multi Mode");
        btn_signup.setId("loginbtn");
        
        // add actions on buttons
        btn_log.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Levels tt = new Levels();
                try {
                    tt.start(ClientApp.mainStage);
                } catch (Exception e) {
                }
            }
        });

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClientApp.multiMain = new MultiMain();
                try {
                    ClientApp.sessionHandler.startMultiGame();
                    ClientApp.multiMain.start(ClientApp.mainStage);
                } catch (Exception e) {
                }
            }
        });

        grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(0, 10, 0, 10));
	    grid.add(btn_log , 0, 0);
	    grid.add(btn_signup , 0 , 1);
	    grid.setAlignment(Pos.CENTER);
	    btn_log.setMaxWidth(Double.MAX_VALUE);
	    btn_signup.setMaxWidth(Double.MAX_VALUE);

        Scene scene = new Scene(grid, 400, 350);
        scene.getStylesheets().add(Sign_up.class.getResource("style.css").toExternalForm());

        primaryStage.setTitle("Game modes");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        grid.requestFocus();
    }
}

