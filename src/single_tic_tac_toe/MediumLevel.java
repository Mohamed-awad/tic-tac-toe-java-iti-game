/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package single_tic_tac_toe;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Dev
 */
public class MediumLevel {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dev
 */
    
    private Boolean playable;
    private Pane root = new Pane();

    private Tile[][] gui_board;
    private char[][] back_end_board;
    private char winner;
    private List<Combo> combos;
    private Combo combo;

    public MediumLevel() {
        playable = true;
        gui_board = new Tile[3][3];
        back_end_board = new char[3][3];
        winner = '-';
        combos = new ArrayList<>();
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private class Tile extends StackPane {
        private Text text;
        private Rectangle border;
        private int row, col;

        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
            text = new Text();
            border = new Rectangle(200, 200);
            border.setFill(null);
            border.setStroke(Color.BLACK);
            text.setFont(Font.font(60));
            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {
                if (!playable) {
                    return;
                }
                if (event.getButton() == MouseButton.PRIMARY) {   //make left click on mouse
                    drawX();
                    checkWin();
                    if (!playable) {
                        return;
                    }
                    computerPlay();
                    checkWin();
                }
            });
        }

        public void computerPlay() {

            //Choose winning move if available
                for (int column = 0; column <= 2; column++) {
                    if (back_end_board[row][column] == '-') {
                        back_end_board[row][column] = 'o';
                        if (checkRows() || checkCols() || checkDs()) {
                            gui_board[row][column].drawO();
                            return;
                        } else {
                            back_end_board[row][column] = '-';
                        }
                    }
                }

            //Choose center if available
            if (back_end_board[1][1] == '-') {
                back_end_board[1][1] = 'o';
                gui_board[1][1].drawO();
                return;
            }

            //Choose a corner if available 
            if (back_end_board[0][0] == '-') {
                back_end_board[0][0] = 'o';
                gui_board[0][0].drawO();
                return;
            }

            if (back_end_board[0][2] == '-') {
                back_end_board[0][2] = 'o';
                gui_board[0][2].drawO();
                return;
            }

            if (back_end_board[2][0] == '-') {
                back_end_board[2][0] = 'o';
                gui_board[2][0].drawO();
                return;
            }

            if (back_end_board[2][2] == '-') {
                back_end_board[2][2] = 'o';
                gui_board[2][2].drawO();
                return;
            }

            //Choose a random move
            for (int row = 0; row <= 2; row++) {
                for (int column = 0; column <= 2; column++) {
                    if (back_end_board[row][column] == '-') {
                        back_end_board[row][column] = 'o';
                        gui_board[row][column].drawO();
                        return;
                    }
                }
            }
        }

        private void drawX() {
            text.setText("x");
            back_end_board[row][col] = 'x';
        }

        private void drawO() {
            text.setText("o");
        }

        public double getCenterX() {
            return getTranslateX() + 100;     //to put x in the center
        }

        public double getCenterY() {
            return getTranslateY() + 100;      //to put o in the center
        }
    }

    private class Combo {

        private Tile[] tiles;

        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }
    }

    private Parent createContent() {
        root.setPrefSize(600, 600);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile(i, j);
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);
                root.getChildren().add(tile);
                gui_board[i][j] = tile;
                back_end_board[i][j] = '-';
            }
        }
        return root;
    }

    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (back_end_board[i][0] == back_end_board[i][1]
                    && back_end_board[i][0] == back_end_board[i][2]
                    && back_end_board[i][0] != '-') {
                combo = new Combo(gui_board[i][0], gui_board[i][1], gui_board[i][2]);
                if (back_end_board[i][0] == 'x') {
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
            if (back_end_board[0][i] == back_end_board[1][i]
                    && back_end_board[0][i] == back_end_board[2][i]
                    && back_end_board[0][i] != '-') {
                combo = new Combo(gui_board[0][i], gui_board[1][i], gui_board[2][i]);
                if (back_end_board[i][0] == 'x') {
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
        if (back_end_board[0][0] == back_end_board[1][1]
                && back_end_board[0][0] == back_end_board[2][2]
                && back_end_board[0][0] != '-') {
            combo = new Combo(gui_board[0][0], gui_board[1][1], gui_board[2][2]);
            if (back_end_board[0][0] == 'x') {
                winner = 'x';
            } else {
                winner = 'o';
            }
            return true;
        }
        if (back_end_board[0][2] == back_end_board[1][1]
                && back_end_board[0][2] == back_end_board[2][0]
                && back_end_board[0][2] != '-') {
            combo = new Combo(gui_board[0][2], gui_board[1][1], gui_board[2][0]);
            if (back_end_board[2][0] == 'x') {
                winner = 'x';
            } else {
                winner = 'o';
            }
            return true;
        }
        return false;
    }

    private void checkWin() {    //check state of the game
        if (checkRows() || checkCols() || checkDs()) {
            playable = false;
            playWinAnimation(combo);
        }
    } 
   
    private void playWinAnimation(Combo combo) {
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[0].getCenterX());
        line.setEndY(combo.tiles[0].getCenterY());
        root.getChildren().add(line);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
                new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
        timeline.play();
    }
}