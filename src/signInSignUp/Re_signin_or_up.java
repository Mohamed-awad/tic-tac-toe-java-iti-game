package signInSignUp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import client.ClientSession;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Re_signin_or_up extends Application {
	public static Stage mainStage;
	public static Socket serverSockett;
	public static ClientSession sessionHandler;
    GridPane grid = new GridPane();
    
    public Re_signin_or_up() throws UnknownHostException, IOException {
    	serverSockett = new Socket("localhost", 5000);
		sessionHandler = new ClientSession(serverSockett);
	}
    
    @Override
    public void start(Stage primaryStage) throws Exception {
          Button btn_log = new Button();
          btn_log.setText("Login");
          btn_log.setId("loginbtn");

          Button btn_signup = new Button();
          btn_signup.setText("Signup");
          btn_signup.setId("loginbtn");
          
          // add actions on buttons
          btn_log.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
  			public void handle(ActionEvent event) {	
  				signin();
  			}
          });
          
          btn_signup.setOnAction(new EventHandler<ActionEvent>() {
          	@Override
    			public void handle(ActionEvent event) {	
    				signup();
    			}
          });
          
          //grid.setGridLinesVisible(true);
          grid.setHgap(10);
          grid.setVgap(10);
          grid.setPadding(new Insets(0, 10, 0, 10));
          grid.add(btn_log , 0, 15);
          grid.add(btn_signup , 0 , 17);
          grid.setAlignment(Pos.CENTER);
          btn_log.setMaxWidth(Double.MAX_VALUE);
          btn_signup.setMaxWidth(Double.MAX_VALUE);
          
          Scene scene = new Scene(grid, 400, 350);
          scene.getStylesheets().add(Sign_up.class.getResource("style.css").toExternalForm());
        
          primaryStage.setTitle("Register");
          mainStage = primaryStage;
          mainStage.setScene(scene);
          mainStage.setResizable(false);
          mainStage.show();
          grid.requestFocus();
    }
    
	public void signin() {
		Sign_in sign_in = new Sign_in();
		try {
			sign_in.start(mainStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void signup() {
		Sign_up sign_up = new Sign_up();
		try {
			sign_up.start(mainStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
