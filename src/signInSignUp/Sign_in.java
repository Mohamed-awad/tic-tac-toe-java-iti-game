package signInSignUp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import client.ClientSession;
import db.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import single_or_multi.ChooseGUI;

public class Sign_in {

    GridPane grid = new GridPane();
    public static Scene scene;
    public void start(Stage primaryStage) {
        //Login btn
        Button btn = new Button();
        btn.setText("Login");
        btn.setId("loginbtn");
        Button back = new Button();
        back.setText("Back");
        back.setId("loginbtn");
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
                if (!(UsText.getText().equals("") || PassText.getText().equals(""))) {
                    try {
                        ClientApp.sessionHandler.login(UsText.getText(), PassText.getText());
                        Thread.sleep(1000);
                        if ("success".equals(ClientApp.sessionHandler.return_response())) {
                            ClientApp.choice = new ChooseGUI();
                            try {
                                ClientApp.choice.start(ClientApp.mainStage);
                            } catch (Exception e) {
                            }
                        } else if ("failed".equals(ClientApp.sessionHandler.return_response())) {
                            showAlert();
                        }
                    } catch (UnknownHostException e) {
                        System.out.println(e);
                    } catch (IOException e) {
                        System.out.println(e);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                } else {
                    showAlert();
                }
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ClientApp ca = new ClientApp();
                    ca.start(ClientApp.mainStage);
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
        // Textfield
        grid.add(UsText, 2, 3);
        grid.add(PassText, 2, 4);
        grid.add(btn, 2, 5);
        grid.add(back, 2, 6);
        back.setMaxWidth(Double.MAX_VALUE);
        btn.setMaxWidth(Double.MAX_VALUE);
        scene = new Scene(grid, 600, 350);
        scene.getStylesheets().add(Sign_up.class.getResource("style.css").toExternalForm());
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        grid.requestFocus();
    }
    private void showAlert() {
        Alert alert = new Alert(AlertType.INFORMATION, "Invalid Username Or Password", ButtonType.CANCEL);
        alert.setTitle("Failed");
        alert.setHeaderText(null);
        alert.setContentText("Invalid Username Or Password");
        alert.show();
    }
}
