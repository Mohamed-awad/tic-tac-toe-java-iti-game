package client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import client.invite.MultiMain;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.assets.Request;
import signInSignUp.ClientApp;
import signInSignUp.Sign_up;

public class TicTacGame {

    GridPane grid = new GridPane();
    private Boolean playable;
    private Boolean your_turn;
    private Combo combo;
    char winner;
    TextArea showMsgsIn;
    Tile board[][];
    public TicTacGame(Boolean x, Boolean y) {
        your_turn = y;
        playable = x;
        board = new Tile[3][3];
    }
    public void start(Stage primaryStage) {
        //chat area
        showMsgsIn = new TextArea();
        showMsgsIn.setEditable(false);
        showMsgsIn.setPrefHeight(400);  //sets height of the TextArea to 400 pixels 
        showMsgsIn.setPrefWidth(600);    //sets width of the TextArea to 300 pixels
        grid.add(showMsgsIn, 40, 9);
        TextField TextInput = new TextField();
        TextInput.setPrefHeight(80);  //sets height of the TextArea to 400 pixels 
        TextInput.setPrefWidth(100);    //sets width of the TextArea to 300 pixels
        TextInput.maxHeight(Double.MAX_VALUE);
        grid.add(TextInput, 40, 10);
        Button SendBtn = new Button();
        SendBtn.setText("Send");
        SendBtn.setId("loginbtn");
        SendBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(SendBtn, 40, 11);
        grid.add(new StackPane(new Text("")), 10, 20);
        SendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!TextInput.getText().equals("")) {
                    showMsgsIn.appendText(TextInput.getText() + "\n");
                    try {
                        ClientApp.sessionHandler.sendMsg(TextInput.getText());
                    } catch (IOException e) {
                        ClientApp.connectionError();
                    }
                    TextInput.setText("");
                }
            }
        });
        //End of chat
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        //grid.setGridLinesVisible(true);
        Scene scene = new Scene(createContent(), 1350, 750);
        scene.getStylesheets().add(Sign_up.class.getResource("style.css").toExternalForm());
        primaryStage.setTitle("Tic Tac - ONline Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        grid.requestFocus();
    }
    public void setMsg(String msg) {
        showMsgsIn.appendText(msg);
    }

    private class Tile extends StackPane {

        private Text text;
        private Rectangle rect;
        private int row, col;
        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
            text = new Text();
            rect = new Rectangle(190, 190);
            rect.setFill(Color.BEIGE);
            rect.setStroke(Color.BLUEVIOLET);
            text.setFont(Font.font(60));
            setAlignment(Pos.CENTER);
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
                                checkWin(true);
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
                                checkWin(false);
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
        public int getCenterX() {
            return (int) getTranslateX() + 95;     //to put x in the center
        }
        public int getCenterY() {
            return (int) getTranslateY() + 95;      //to put o in the center
        }
    }

    private class Combo {

        private Tile[] tiles;
        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }
    }
    public void setMove(int x, int y, String current_player) {
        your_turn = !your_turn;
        if (current_player.equals("X")) {
            board[x][y].drawX();
        } else {
            board[x][y].drawO();
        }
    }
    private Parent createContent() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile(i, j);
                board[i][j] = tile;
                grid.add(tile, 33 + j, 7 + i);
            }
        }
        return grid;
    }
    private void checkWin(boolean win) {    //check state of the game
        if (checkRows() || checkCols() || checkDs()) {
            //playWinAnimation(combo);
            if (win) {
            } else {
            }
        }
    }
    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].get_value().equals(board[i][1].get_value())
                    && board[i][0].get_value().equals(board[i][2].get_value())
                    && !board[i][0].get_value().equals("")) {
                combo = new Combo(board[i][0], board[i][1], board[i][2]);
                if (board[i][0].get_value().equals("X")) {
                    winner = 'x';
                } else {
                    winner = 'o';
                }
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
                combo = new Combo(board[0][i], board[1][i], board[2][i]);
                if (board[i][0].get_value().equals("X")) {
                    winner = 'x';
                } else {
                    winner = 'o';
                }
                return true;
            }
        }
        return false;
    }
    private boolean checkDs() {
        if (board[0][0].get_value().equals(board[1][1].get_value())
                && board[0][0].get_value().equals(board[2][2].get_value())
                && !board[0][0].get_value().equals("")) {
            combo = new Combo(board[0][0], board[1][1], board[2][2]);
            if (board[0][0].get_value().equals("x")) {
                winner = 'x';
            } else {
                winner = 'o';
            }
            return true;
        }
        if (board[0][2].get_value().equals(board[1][1].get_value())
                && board[0][2].get_value().equals(board[2][0].get_value())
                && !board[0][2].get_value().equals("")) {
            combo = new Combo(board[0][2], board[1][1], board[2][0]);
            if (board[2][0].get_value().equals("x")) {
                winner = 'x';
            } else {
                winner = 'o';
            }
            return true;
        }
        return false;
    }
    private void playWinAnimation(Combo combo) {
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[2].getCenterX());
        line.setEndY(combo.tiles[2].getCenterY());
        grid.add(line, 40, 10);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
                new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
        timeline.play();
    }
    
}
