package client.invite;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Invite {
    
    
    public void start(Stage primaryStage) {
        
        //create and set the grid
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding(new Insets(30, 20, 30, 20));
        
        //create the nodes
        Label invitePeople = new Label("People To Invite");
        Label peopleinvited = new Label("People Who Invited You");
        Button inviteBtn = new Button("Invite");
        Button acceptBtn = new Button("Accept");
        Button declineBtn = new Button("Decline");
        
        // send invitation list and accept invitation list
        ObservableList <String> sendIvitationObservableList = FXCollections.observableArrayList("Ziyad","Ahmed","Kholaif","Ziyad","Ahmed","Kholaif","Ziyad","Ahmed","Kholaif","Ziyad","Ahmed","Kholaif","Ziyad","Ahmed","Kholaif");
        ListView <String> invitePeopleListView = new ListView<String>(sendIvitationObservableList);
        invitePeopleListView.setPrefSize(300,300);
        invitePeopleListView.setOrientation(Orientation.VERTICAL);
        MultipleSelectionModel <String> sendInvitationModule = invitePeopleListView.getSelectionModel();
                  sendInvitationModule.selectedItemProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, 
                        String old_val, String new_val) {
                        System.out.println(new_val);
                        System.out.println(old_val);
                    }
                });
        ObservableList <String> AcceptInvitationObserveList = FXCollections.observableArrayList("Esraa","Eman","Hesham");
        ListView <String> AcceptInvitationListView = new ListView<String>(AcceptInvitationObserveList);
        AcceptInvitationListView.setPrefSize(300,300);
        AcceptInvitationListView.setOrientation(Orientation.VERTICAL);
        MultipleSelectionModel <String> lvModule = AcceptInvitationListView.getSelectionModel();
                  lvModule.selectedItemProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov, 
                        String old_val, String new_val) {
                        System.out.println(new_val);
                    }
                });
     
                  
                  
        //added nodes of grid
        grid.add(invitePeople, 0, 0);
        grid.add(invitePeopleListView,0,1);
        grid.add(inviteBtn, 0, 2);
        grid.add(peopleinvited, 4,0);
        grid.add(AcceptInvitationListView,4,1);
        grid.add(acceptBtn,4,2);
        grid.add(declineBtn,4,3);
     
        //set alignment
        grid.setHalignment(invitePeople, HPos.CENTER);
        grid.setHalignment(peopleinvited, HPos.CENTER);
        grid.setHalignment(inviteBtn, HPos.CENTER);
        grid.setHalignment(acceptBtn, HPos.CENTER);
        grid.setHalignment(declineBtn, HPos.CENTER);
 
        
        //setting the stage & scene
        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
 //      ImageView imageChart = new ImageView(
//      new Image(LayoutSample.class.getResourceAsStream("graphics/piechart.png")));
//      grid.add(imageChart, 1, 2, 2, 1);

    
    }
}
