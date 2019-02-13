package client.invite;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.ClientSession;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import server.Player;
import server.Server;
import server.assets.Request;
import signInSignUp.Re_signin_or_up;

public class Invite extends Thread{
    
	String current;
	ArrayList<String> online_players;
	ArrayList<String> players_invite_me;
	ObservableList <String> sendIvitationObservableList;
	ListView <String> invitePeopleListView;
	ObservableList <String> AcceptInvitationObserveList;
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
        sendIvitationObservableList = FXCollections.observableArrayList();
		try {
			Re_signin_or_up.sessionHandler.get_online_players();
            Thread.sleep(1000);
            online_players =  Re_signin_or_up.sessionHandler.return_online_players();
            for (int i = 0; i < online_players.size(); i++) {
            	sendIvitationObservableList.add(online_players.get(i));
    		}
            current = online_players.get(0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        invitePeopleListView = new ListView<String>(sendIvitationObservableList);
        invitePeopleListView.setPrefSize(300,300);
        invitePeopleListView.setOrientation(Orientation.VERTICAL);
        MultipleSelectionModel <String> sendInvitationModule = invitePeopleListView.getSelectionModel();
        sendInvitationModule.selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, 
                String old_val, String new_val) {
                current = new_val;
            }
        });
        
        AcceptInvitationObserveList = FXCollections.observableArrayList();
        ListView <String> AcceptInvitationListView = new ListView<String>(AcceptInvitationObserveList);
        AcceptInvitationListView.setPrefSize(300,300);
        AcceptInvitationListView.setOrientation(Orientation.VERTICAL);
        MultipleSelectionModel <String> lvModule = AcceptInvitationListView.getSelectionModel();
	    lvModule.selectedItemProperty().addListener(new ChangeListener<String>() {
	        public void changed(ObservableValue<? extends String> ov, 
	            String old_val, String new_val) {
	        }
	    });
     
	    // add action
	    inviteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        		try {
        			Re_signin_or_up.sessionHandler.sendInvitation(current);
                    showAlert("invitation sent successfully we will inform you if what is response");
                    Thread.sleep(1000);
        		} catch (UnknownHostException e) {
        			e.printStackTrace();
        		} catch (IOException e) {
        			e.printStackTrace();
        		} catch (InterruptedException e) {
        			e.printStackTrace();
        		}
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
        start();
    }
    
    public void showAlert(String mess) {
        Alert alert = new Alert(AlertType.INFORMATION, mess  , ButtonType.CANCEL);
        alert.setTitle("Invitation");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.show();
    }

	@Override
	public void run() {
		while (true) {
			try {
				Re_signin_or_up.sessionHandler.get_online_players();
				Thread.sleep(10000);
				online_players = Re_signin_or_up.sessionHandler.return_online_players();
				sendIvitationObservableList.clear();
	            for (int i = 0; i < online_players.size(); i++) {
	            	sendIvitationObservableList.add(online_players.get(i));
	    		}
	            players_invite_me =  Re_signin_or_up.sessionHandler.return_players_invite_me();
	            AcceptInvitationObserveList.clear();
	            System.out.println(players_invite_me.size());
	            for (int i = 0; i < players_invite_me.size(); i++) {
	            	AcceptInvitationObserveList.add(players_invite_me.get(i));
	    		}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            
            
        }
		
	}
}
