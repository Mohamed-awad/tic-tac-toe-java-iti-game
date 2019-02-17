package client;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.assets.Request;
import server.assets.RequestType;
import signInSignUp.ClientApp;
import signInSignUp.Sign_up;

public class TicTacGame {

    public Scene scene;
    GridPane grid = new GridPane();
    private Boolean playable;
    private Boolean your_turn;
    TextArea showMsgsIn;
    TextField TextInput;
    Tile board[][];
    public TicTacGame(Boolean x, Boolean y) {
        your_turn = y;
        playable = x;
        board = new Tile[3][3];
        showMsgsIn = new TextArea();
        TextInput = new TextField();
    }
    public void start(Stage primaryStage) {
        //chat area
        showMsgsIn.setId("msgchatarea");
        showMsgsIn.setEditable(false);
        showMsgsIn.setPrefHeight(400);  //sets height of the TextArea to 400 pixels 
        showMsgsIn.setPrefWidth(600);    //sets width of the TextArea to 300 pixels
        grid.add(showMsgsIn, 22, 1, 1, 3);
        TextInput.setId("msgsendarea");
        TextInput.setPrefHeight(65);  //sets height of the TextArea to 400 pixels 
        TextInput.maxHeight(Double.MAX_VALUE);
        grid.add(TextInput, 22, 1, 1, 22);
        Button SendBtn = new Button();
        SendBtn.setText("Send");
        SendBtn.setId("Send");
        SendBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(SendBtn, 22, 2, 1, 28);
        SendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!TextInput.getText().equals("")) {
                    showMsgsIn.appendText(TextInput.getText() + "\n");
                    try {
                        ClientApp.sessionHandler.sendMsg(TextInput.getText() + "\n");
                    } catch (IOException e) {
                        ClientApp.connectionError();
                    }
                    TextInput.setText("");
                }
            }
        });
        Button logout = new Button();
        logout.setText("Back");
        logout.setId("logout");
        logout.setMaxWidth(Double.MAX_VALUE);
        grid.add(logout, 22, 4, 1, 16);
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(() -> {
                    try {
                        ClientApp.multiMain.start(ClientApp.mainStage);
                        ClientApp.sessionHandler.quitGame();
                        ClientApp.sessionHandler.startMultiGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        Label status = new Label("Player Turn");
        status.setId("status");
        status.setAlignment(Pos.CENTER);
        grid.add(status, 22, 2, 1, 40);
        grid.add(new StackPane(new Text("")), 10, 20);
        //End of chat
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        Scene scene = new Scene(createContent(), 1150, 600);
        scene.getStylesheets().add(Sign_up.class.getResource("GameStyle.css").toExternalForm());
        primaryStage.setTitle("Tic Tac - ONline Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        grid.requestFocus();
    }

    private class Tile extends StackPane {

        private Text text;
        private Rectangle rect;
        private int row, col;
        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
            text = new Text();
            rect = new Rectangle(165, 165);
            rect.setId("rect");
            rect.setArcHeight(45.0d);
            rect.setArcWidth(45.0d);
            rect.setFill(Color.rgb(110, 54, 41, 0.7));
            rect.setStroke(Color.rgb(131, 159, 14));
            text.setFont(Font.font(60));
            getChildren().addAll(rect, text);
            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {   //make left click on mouse
                    if (playable) {
                        if (your_turn) {
                            if (text.getText() == "") {
                                your_turn = false;
                                drawX();
                                try {
                                    ClientApp.sessionHandler.sendMove(this.row, this.col, "X");
                                } catch (IOException e) {
                                    ClientApp.connectionError();
                                }
                                try {
									checkWin();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                            }
                        }
                    } else {
                        if (!your_turn) {
                            if (text.getText() == "") {
                                your_turn = true;
                                drawO();
                                try {
                                    ClientApp.sessionHandler.sendMove(this.row, this.col, "O");
                                } catch (IOException e) {
                                    ClientApp.connectionError();
                                }
                                try {
									checkWin();
								} catch (IOException e) {
									e.printStackTrace();
								}
                            }
                        }
                    }
                }
            });
        }
        public void drawX() {
            text.setText("x");
        }
        public void drawO() {
            text.setText("o");
        }
        public String get_value() {
            return text.getText();
        }
    }
    public void setMsg(String msg) {
        showMsgsIn.appendText(msg);
    }
    private Parent createContent() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile(i, j);
                board[i][j] = tile;
                grid.add(tile, 15 + j, 2 + i);
            }
        }
        return grid;
    }
    public void setMove(int x, int y, String current_player) {
        your_turn = !your_turn;
        if (current_player.equals("X")) {
            board[x][y].drawX();
        } else {
            board[x][y].drawO();
        }
    }
    private void checkWin() throws IOException {    //check state of the game
        if (checkRows() || checkCols() || checkDs()) {
            ClientApp.sessionHandler.sendWin();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Win");
            alert.setHeaderText(null);
            alert.setContentText("Congratulaion you win");
            Optional<ButtonType> result = alert.showAndWait();
            System.out.println(result.get());
            if (result.get() == ButtonType.OK) {
            	ClientApp.multiMain.start(ClientApp.mainStage);
            	ClientApp.sessionHandler.startMultiGame();
            }
        }
    }
    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].get_value().equals(board[i][1].get_value())
                    && board[i][0].get_value().equals(board[i][2].get_value())
                    && !board[i][0].get_value().equals("")) {
                return true;
            }
        }
        return false;
    }
    private boolean checkCols() {
        for (int i = 0; i < 3; i++) {
            if (board[0][i].get_value().equals(board[1][i].get_value())
                    && board[0][i].get_value().equals(board[2][i].get_value())
                    && !board[0][i].get_value().equals("")) {
                return true;
            }
        }
        return false;
    }
    private boolean checkDs() {
        if (board[0][0].get_value().equals(board[1][1].get_value())
                && board[0][0].get_value().equals(board[2][2].get_value())
                && !board[0][0].get_value().equals("")) {
            return true;
        }
        if (board[0][2].get_value().equals(board[1][1].get_value())
                && board[0][2].get_value().equals(board[2][0].get_value())
                && !board[0][2].get_value().equals("")) {
            return true;
        }
        return false;
    }
    public void disconnectGame() throws IOException{
        Request r = new Request(RequestType.END_GAME);
        ClientApp.sessionHandler.sendingStream.writeObject(r);
    }
}
