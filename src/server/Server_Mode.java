package server;

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

public class Server_Mode extends Application {

    Server myServer;
    GridPane grid = new GridPane();
    @Override
    public void start(Stage primaryStage) throws Exception {
        myServer = new Server();
        Button btn_log = new Button();
        btn_log.setText("Start Server");
        btn_log.setId("loginbtn");
        Button btn_signup = new Button();
        btn_signup.setText("Stop Server");
        btn_signup.setId("loginbtn");
        // add actions on buttons
        btn_log.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myServer.startServer();
                grid.getChildren().remove(btn_log);
                grid.add(btn_signup, 0, 0);
            }
        });
        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myServer.stopServer();
                grid.getChildren().remove(btn_signup);
                grid.add(btn_log, 0, 0);
            }
        });
        //grid.setGridLinesVisible(true);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.add(btn_log, 0, 0);
        //grid.add(btn_signup , 0 , 1);
        grid.setAlignment(Pos.CENTER);
        btn_log.setMaxWidth(Double.MAX_VALUE);
        btn_signup.setMaxWidth(Double.MAX_VALUE);
        Scene scene = new Scene(grid, 400, 500);
        scene.getStylesheets().add(Sign_up.class.getResource("style.css").toExternalForm());
        primaryStage.setTitle("Server Mode");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        grid.requestFocus();
    }
    public void stop() throws IOException {
        myServer.stopServer();
        Platform.exit();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
