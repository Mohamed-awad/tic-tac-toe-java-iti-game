package client.clientGUI;

import java.net.UnknownHostException;

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
import client.clientGUI.ChooseGUI;
import client.singleGame.EasyLevel;
import client.singleGame.MediumLevel;
import client.singleGame.TicTacToe;

public class Levels {

    GridPane grid = new GridPane();

    public void start(Stage primaryStage) throws Exception {

    	Button Easy = new Button();
        Easy.setText("Easy");
        Easy.setId("loginbtn");
        
        Button Meduim = new Button();
        Meduim.setText("Meduim");
        Meduim.setId("loginbtn");
        
        Button Hard = new Button();
        Hard.setText("Hard");
        Hard.setId("loginbtn");
        
        Button Back = new Button();
        Back.setText("Back");
        Back.setId("loginbtn");

        // add actions on buttons
        Easy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	EasyLevel el = new EasyLevel();
                try {
                    el.start(ClientApp.mainStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        Meduim.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	MediumLevel ml = new MediumLevel();
                try {
                    ml.start(ClientApp.mainStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        Hard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	TicTacToe tt = new TicTacToe();
                try {
                    tt.start(ClientApp.mainStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        Back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                	ChooseGUI ch = new ChooseGUI();
					ch.start(ClientApp.mainStage);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.add(Easy , 0, 0);
        grid.add(Meduim , 0 , 1);
        grid.add(Hard , 0 , 2);
        grid.add(Back , 0 , 3);
        grid.setAlignment(Pos.CENTER);
        Easy.setMaxWidth(Double.MAX_VALUE);
        Meduim.setMaxWidth(Double.MAX_VALUE);
        Hard.setMaxWidth(Double.MAX_VALUE);
        Back.setMaxWidth(Double.MAX_VALUE);

        Scene scene = new Scene(grid, 400, 500);
        scene.getStylesheets().add(Sign_up.class.getResource("style.css").toExternalForm());

        primaryStage.setTitle("Game Levels");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        grid.requestFocus();
    }
}
  
