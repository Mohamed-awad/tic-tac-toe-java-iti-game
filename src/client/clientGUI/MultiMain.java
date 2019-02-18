package client.clientGUI;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import client.clientGUI.ClientApp;
import client.clientGUI.Sign_up;
import client.clientGUI.ChooseGUI;

public class MultiMain{

    String current;
    String currentInvitation;
    ArrayList<String> online_players;
    ArrayList<String> players_invite_me;
    public ObservableList<String> sendIvitationObservableList;
    public ObservableList<String> OFFlinePeople;
    ArrayList<String> Off_players;
    ListView<String> invitePeopleListView;
    public ObservableList<String> AcceptInvitationObserveList;
    public void start(Stage primaryStage) {
        //Send to online players a hint that i am online 
        //create and set the grid
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding(new Insets(30, 20, 30, 20));
        //create the nodes
        Label invitePeople = new Label("People To Invite");
        invitePeople.setId("Label_Invite");
        
        Label peopleinvited = new Label("People Who Invited You");
        peopleinvited.setId("Label_Invite");
        Label OFF_People = new Label("Offline People");

        OFF_People.setId("Label_Invite");
        
        
        Button inviteBtn = new Button("Invite");
        inviteBtn.setId("InviteBtn");
        
        Button acceptBtn = new Button("Accept");
        acceptBtn.setId("AcceptBtn");
      
        Button backBtn = new Button("Back");
        backBtn.setId("AcceptBtn");

        // send invitation list and accept invitation list
        sendIvitationObservableList = FXCollections.observableArrayList();
        invitePeopleListView = new ListView<String>(sendIvitationObservableList);
        invitePeopleListView.setId("listinvited");
        
        invitePeopleListView.setPrefSize(300, 300);
        invitePeopleListView.setOrientation(Orientation.VERTICAL);
        MultipleSelectionModel<String> sendInvitationModule = invitePeopleListView.getSelectionModel();
        sendInvitationModule.selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                    String old_val, String new_val) {
                current = new_val;
            }
        });
        // Offline People
        OFFlinePeople = FXCollections.observableArrayList();
        
        ListView<String> Off_players = new ListView<String>(OFFlinePeople);
        Off_players.setId("listinvited");
        Off_players.setPrefSize(300, 300);
        Off_players.setOrientation(Orientation.VERTICAL);
        MultipleSelectionModel<String> lvModule = Off_players.getSelectionModel();
        lvModule.selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                    String old_val, String new_val) {
                currentInvitation = new_val;
            }
        });

        AcceptInvitationObserveList = FXCollections.observableArrayList();
        ListView<String> AcceptInvitationListView = new ListView<String>(AcceptInvitationObserveList);
        AcceptInvitationListView.setId("listinvited");
        AcceptInvitationListView.setPrefSize(300, 300);
        AcceptInvitationListView.setOrientation(Orientation.VERTICAL);
        MultipleSelectionModel<String> lv1Module = AcceptInvitationListView.getSelectionModel();
        lv1Module.selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                    String old_val, String new_val) {
                currentInvitation = new_val;
            }
        });
        // add action
        inviteBtn.setOnAction((event) -> {
            try {
                if (current != null) {
                    ClientApp.sessionHandler.sendInvitation(current);
                }
                current = null;
            } catch (IOException ex) {
                Logger.getLogger(MultiMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            showAlert("invitation sent successfully we will inform you if what is response");
        });
        acceptBtn.setOnAction((event) -> {
            if (currentInvitation != null) {
                try {
                    ClientApp.sessionHandler.sendReply(currentInvitation, "accept");
                    currentInvitation = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        backBtn.setOnAction((event) -> {
            try {

            	ChooseGUI ca = new ChooseGUI();
				ca.start(ClientApp.mainStage);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
        });
        //added nodes of grid
        grid.add(invitePeople, 0, 0);
        grid.add(invitePeopleListView, 0, 1);
        grid.add(inviteBtn, 0, 2);
        grid.add(peopleinvited, 4, 0);
        grid.add(OFF_People, 0, 3);
        grid.add(AcceptInvitationListView, 4, 1);
        grid.add(acceptBtn, 4, 2);

        grid.add(backBtn, 4, 3);
        grid.add(Off_players, 0, 4 , 1 , 5);
        
        //set alignment
        grid.setHalignment(invitePeople, HPos.CENTER);
        grid.setHalignment(peopleinvited, HPos.CENTER);
        grid.setHalignment(inviteBtn, HPos.CENTER);
        grid.setHalignment(acceptBtn, HPos.CENTER);

        //setting the stage & scene
        Scene scene = new Scene(grid, 600, 600);
        scene.getStylesheets().add(Sign_up.class.getResource("GameStyle.css").toExternalForm());
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
    public void showAlert(String mess) {
        Alert alert = new Alert(AlertType.INFORMATION, mess, ButtonType.CANCEL);
        alert.setTitle("Invitation");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.show();
    }
    
}
