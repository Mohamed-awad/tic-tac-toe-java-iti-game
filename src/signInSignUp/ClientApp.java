package signInSignUp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import client.ClientSession;
import client.TicTacGame;
import client.invite.MultiMain;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import single_or_multi.ChooseGUI;

public class ClientApp extends Application {

    public static Stage mainStage;
    public static Socket serverSockett;
    public static ClientSession sessionHandler;
    public static MultiMain multiMain;
    public static ChooseGUI choice;
    public static TicTacGame game;
    GridPane grid = new GridPane();
    public static String gameArr[];
    public ClientApp() throws UnknownHostException {
        try {
            serverSockett = new Socket("localhost", 5000);
            sessionHandler = new ClientSession(serverSockett);
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Connection problem");
            alert.setHeaderText(null);
            alert.setContentText("Connection lost");
            Optional<ButtonType> result = alert.showAndWait();
            System.out.println(result.get());
            if (result.get() == ButtonType.OK) {
                Platform.exit();
            }
        }
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
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        grid.add(btn_log , 0, 3);
        grid.add(btn_signup , 0 , 5);

        grid.setAlignment(Pos.CENTER);
        btn_log.setMaxWidth(Double.MAX_VALUE);
        btn_signup.setMaxWidth(Double.MAX_VALUE);
        Scene scene = new Scene(grid, 400, 350);
        scene.getStylesheets().add(Sign_up.class.getResource("style.css").toExternalForm());
        primaryStage.setTitle("Register");
        primaryStage.setResizable(false);
        mainStage = primaryStage;
        mainStage.setScene(scene);
        mainStage.show();
        grid.requestFocus();
    }
    public static void signin() {
        Sign_in sign_in = new Sign_in();
        try {
            sign_in.start(mainStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void signup() {
        Sign_up sign_up = new Sign_up();
        try {
            sign_up.start(mainStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void changeStageSize(Window stage, int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }
    @Override
    public void stop() throws IOException {
        try {
            game.disconnectGame(); //if the player is in a game with another player if it's not it will continue
        } catch (Exception e) {
        }
        sessionHandler.endConnection();
        Platform.exit();
    }
    public static void alert_loser() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lose");
        alert.setHeaderText(null);
        alert.setContentText("try another time");
        Optional<ButtonType> result = alert.showAndWait();
        System.out.println(result.get());
        if (result.get() == ButtonType.OK) {
            ClientApp.multiMain.start(ClientApp.mainStage);
            ClientApp.sessionHandler.startMultiGame();
        }
    }
    public static void connectionError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connection problem");
        alert.setHeaderText(null);
        alert.setContentText("Player Has disconnected, game has been saved");
        Optional<ButtonType> result = alert.showAndWait();
        System.out.println(result.get());
        if (result.get() == ButtonType.OK) {
            try {
                ClientApp.multiMain.start(ClientApp.mainStage);
                ClientApp.sessionHandler.startMultiGame(); // if other player disconnected
            } catch (IOException ex) {
                disconnectServer();
            }
        }
    }
    public static void disconnectServer() { //WHEN server disconnect 
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connection problem");
        alert.setHeaderText(null);
        alert.setContentText("Disconnect from server");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
        };
    }
    public static void removeBusyPlayer(String p) { // Remove player from list when he logout or start playing
        if (multiMain.AcceptInvitationObserveList.contains(p)) {
            multiMain.AcceptInvitationObserveList.remove(p);
        }
        if (multiMain.sendIvitationObservableList.contains(p)) {
            multiMain.sendIvitationObservableList.remove(p);
            System.out.println((String) p);
        }
    }
    public static void repeated(String msg, String mode) { //WHEN server disconnect 
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        switch (mode) {
            case "signin":
                ClientApp.mainStage.setScene(Sign_in.scene);
                ClientApp.signin();
                break;
            case "signup":
                ClientApp.signup();
                break;
        }
        alert.showAndWait();
    }
    public static void showSavedGameExist() { //WHEN server disconnect 
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("There is a saved Game");
        alert.setHeaderText(null);
        alert.setContentText("You will continue the past game");
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
