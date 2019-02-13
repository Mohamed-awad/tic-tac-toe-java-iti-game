/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.assets.Request;

/**
 *
 * @author Dev
 */

public class TicTacGame1 extends Application {

      
      
      private ClientSession client ;

      
      private boolean playable = true;                             //flag for playing 
      private boolean turnX = true;                           //flaaag for X or O
      private Tile[] board = new Tile[9];
      private List<Combo> combos = new ArrayList<>();  //

      //parent scene
      // board pane
      private Pane root = new Pane();

      //chat pane
      private BorderPane chatPane = new BorderPane();
      private BorderPane ch1 = new BorderPane();
      Button btnChat = new Button("Send");
      TextArea txtArea; TextField txtField;
      //score pane
      private FlowPane scorePane = new FlowPane();
      Label l1=new Label();
      Label l2=new Label();
      Button  butStartOver = new Button ("Start Over");
      Line line ;
      
      //main game window
      private BorderPane mainGame = new BorderPane();

      @Override
      public void init() throws IOException {
         
            //connection 
            Socket serverSocket = new Socket("localhost", 5000);
            client = new ClientSession(serverSocket);
          
            // gui
            root.setPrefSize(600, 600);

            //chat area
            
            txtArea = new TextArea();
            txtArea.setEditable(false);
            txtField = new TextField();
            chatPane.setLeft(txtArea);
            ch1.setCenter(txtField);
            ch1.setRight(btnChat);
            chatPane.setBottom(ch1);
           
            // score Area 
            
            scorePane.getChildren().addAll(new Text("Player X :"),l1);
           scorePane.setAlignment(Pos.CENTER);
            scorePane.getChildren().addAll(new Text("Player O :"),l2);
      }
      

      private Parent createContent() {
        
            int j = 0;
            for (int i = 0; i < 9; i++) {

                  Tile tile = new Tile();

                  tile.setTranslateX(i % 3 * 200);
                  tile.setTranslateY(200 * j);
                  if (i == 2) {
                        j++;
                  }
                  if (i == 5) {
                        j++;
                  }

                  tile.num = i;
                  root.getChildren().add(tile);
                  board[i] = tile;
            }
            //horizontal
            for (int y = 0; y < 3; y++) {
                  combos.add(new Combo(board[y * 3], board[y * 3 + 1], board[y * 3 + 2]));
            }
            //vertical
            for (int x = 0; x < 3; x++) {
                  combos.add(new Combo(board[x], board[3 + x], board[6 + x]));
            }
            //diagonals

            combos.add(new Combo(board[0], board[4], board[8]));
            combos.add(new Combo(board[2], board[4], board[6]));

            mainGame.setCenter(root);
            mainGame.setRight(chatPane);
            
//             chatPane.setMaxWidth(50);
            chatPane.setMaxHeight(400);
            
            scorePane.setPrefSize(100,200);
            scorePane.getChildren().add(butStartOver);
            mainGame.setLeft(scorePane);
            mainGame.setBottom(new Text("Status"));
            mainGame.setTop(new Text("TicTacToe"));
            return mainGame;
      }

      @Override
      public void start(Stage primaryStage) {
            
            primaryStage.setScene(new Scene(createContent()));
//          btnChat.setOnAction(new EventHandler<ActionEvent>() {
//               @Override
//               public void handle(ActionEvent event) {
//                    Stage chat = new Stage();
//                    Text x1 = new Text("Play");
//                    StackPane x = new StackPane(x1);
//                    Scene s = new Scene(x);
//                    primaryStage.setScene(s);
////                primaryStage.show();
//               }
//          });
//          new Thread(()->{
//                wait();
//                Request request = client.request;
//                switch (request.getType()) {
//                      case RECEIVE_MSG :
//                            // print in gui
//                            
//                            break;
//                }
//                
//          }) .start();
          butStartOver.setOnAction((ActionEvent event) ->{
             if (!playable) {
                playable = true;
                  for(Tile t : board) {
                     t.text.setText(""); 
               }
               for (Combo c: combos) {
//                     c.tiles = null ;
               }
               l1.setText(" ");
               l2.setText(" ");    
               line.resize(0, 0);
               root.getChildren().remove(line);
                      }
          });
          
          primaryStage.setResizable(false);
            primaryStage.show();
            
      }

      private void checkState() {    //check state of the game
            int i=0;
            for (Combo combo : combos) {
                  if (combo.isComplete()) {
                        playable = false;
                        playWinAnimation(combo);
                        break;
                  }
//                  for(Tile t : combo.tiles){
//                        if (t.text.getText() !="" ) {
//                              i++;
//                              if(i==9) playable = false;
//                        } 
//                  }
            }
      }
      private void playWinAnimation(Combo combo) {
            if( combo.tiles[0].text.getText() == "x") l1.setText("Winner");
            else l2.setText("Winner");
            
            line = new Line();
            line.setStartX(combo.tiles[0].getCenterX());
            line.setStartY(combo.tiles[0].getCenterY());
            line.setEndX(combo.tiles[0].getCenterX());
            line.setEndY(combo.tiles[0].getCenterY());
            line.setStrokeWidth(5);
            root.getChildren().add(line);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                   new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
                   new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
            timeline.play();
         
      }

      private class Combo {

            private Tile[] tiles;

            public Combo(Tile... tiles) {
                  this.tiles = tiles;
            }

            public boolean isComplete() {
                  if (tiles[0].getValue().isEmpty()) {
                        return false;
                  }
                  return tiles[0].getValue().equals(tiles[1].getValue())
                         && tiles[0].getValue().equals(tiles[2].getValue());      ////compare
            }
      }

      private class Tile extends StackPane {

            private Text text = new Text("");
            private int num;

            public Tile() {
                  Rectangle border = new Rectangle(200, 200);
                  border.setFill(null);
                  border.setStroke(Color.BLACK);
                  text.setFont(Font.font(60));
                  setAlignment(Pos.CENTER);
                  getChildren().addAll(border, text);

                  //Action on click
                  setOnMouseClicked(event -> {
                        if (playable && event.getButton() == MouseButton.PRIMARY) {
                           //make left click on mouse
                              if (!turnX) {
                                    return;
                              }
                              if (text.getText() == "") {
                                    System.out.println(this.num);
                                    drawX();
                                    turnX = false;
                                    checkState();
                              }
                        }
                        else if (event.getButton() == MouseButton.SECONDARY) {   //make right click on mouse
                              if (turnX) {
                                    return;
                              }
                              if ("".equals(text.getText())) {
                                    System.out.println(this.num);
                                    drawO();
                                    turnX = true;
                                    checkState();
                              }
                        }
                        
                  });
            }

            public double getCenterX() {
                  return getTranslateX() + 100;     //to put x in the center
            }

            public double getCenterY() {
                  return getTranslateY() + 100;      //to put o in the center
            }

            public String getValue() {
                  return text.getText();
            }

            private void drawX() {

                  text.setText("x");
            }

            private void drawO() {
                  text.setText("o");
            }
      }
      
      public void chatR (String s) {
            txtArea.appendText(s);
      }
      /**
       * @param args the command line arguments
       */
      public static void main(String[] args) {
            launch(args);
      }
}
