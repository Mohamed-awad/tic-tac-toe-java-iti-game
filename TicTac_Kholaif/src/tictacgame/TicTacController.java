/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictacgame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author A.Kholaif
 */
public class TicTacController implements Initializable {

    /**
     * Initializes the controller class.
     */
//    @FXML private TextArea txtArea;
//    @FXML private TextField txtF;
//    @FXML private Button but;
    
    @FXML private GridPane p;
    
    @FXML private Pane p1,p2,p3,p4,p5,p6,p7,p8,p9;

    public void paneAction1 (){
        System.out.println(GridPane.getColumnIndex(p1));
        System.out.println(GridPane.getRowIndex(p1));
    }
    public void paneAction2 (){
        System.out.println(GridPane.getColumnIndex(p2));
        System.out.println(GridPane.getRowIndex(p2));
    }
    public void paneAction3 () {
        System.out.println(GridPane.getColumnIndex(p3));
        System.out.println(GridPane.getRowIndex(p3));

    }
    public void paneAction4 (){
        
    }
    public void paneAction5 (){
        
    }
    public void paneAction6 (){
        
    }
    public void paneAction7 (){
        
    }
    public void paneAction8 (){
        
    }
    public void paneAction9 (){
        
    }
//    public void p1Info() {
//        System.out.println(p1.getId());
//        
//        System.out.println();
//    }
//    public void getInfo () {
////        System.out.println(p1.getId());
//        System.out.println(this.getId());
//    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
