package signInSignUp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import client.ClientSession;
import db.DB;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Sign_up {
     GridPane grid = new GridPane();
    
    public void start(Stage primaryStage) {
        //Login btn
        Button btn = new Button();
        btn.setText("Sign up");
        btn.setId("loginbtn");
        // labels
          
        // text field
        TextField UsText = new TextField(); 
        UsText.setId("UsText");
        UsText.setPromptText("Enter your Name");
        PasswordField PassText = new PasswordField(); 
        PassText.setId("PassText");
        PassText.setPromptText("Enter your Password");
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(!(UsText.getText().equals("") || PassText.getText().equals("")))
            	{
            		Socket serverSockett;
            		ClientSession sessionHandler;
					try {
						serverSockett = new Socket("localhost", 5000);
						sessionHandler = new ClientSession(serverSockett);
						sessionHandler.signup(UsText.getText(), PassText.getText());
						Thread.sleep(1000);
						if(sessionHandler.return_response())
						{
							Sign_in sign_in = new Sign_in();
							showAlert("Rigistration Successeded");
							try {
								sign_in.start(Re_signin_or_up.mainStage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else
						{
							showAlert("Rigistration Failed");
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
            	}
            	else {
            		showAlert("Username Or PassWord can`t be empty");
            	}
            }
        });
        
          grid.setHgap(10);
          grid.setVgap(10);
          grid.setPadding(new Insets(0, 10, 0, 10));
         //grid.setGridLinesVisible(true);
          // label
          
          // Textfield
          grid.add(UsText , 0 , 13);
          grid.add(PassText ,0  , 14);
          grid.add(btn , 0, 15);
          grid.setAlignment(Pos.CENTER);
          btn.setMaxWidth(Double.MAX_VALUE);
          
         Scene scene = new Scene(grid, 400, 350);
         scene.getStylesheets().add(Sign_up.class.getResource("style.css").toExternalForm());
        
         primaryStage.setTitle("Sign up");
         primaryStage.setScene(scene);
         primaryStage.setResizable(false);
         primaryStage.show();
         grid.requestFocus();
         
    }

    private void showAlert(String mess) {
        Alert alert = new Alert(AlertType.INFORMATION, mess  , ButtonType.CANCEL);
        alert.setTitle("Succedded");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.show();
    }
}
