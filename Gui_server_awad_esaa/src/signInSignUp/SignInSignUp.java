package signInSignUp;


import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SignInSignUp extends Application{

	DB new_db;
	
	BorderPane pane;
	Scene scene;
	Button signin_btn;
	Button signup_btn;
	Button submit_btn;
	Button submit_btn1;
	TextField username;
	PasswordField password;
	
	
	public SignInSignUp() {
		pane = new BorderPane();
		scene = new Scene(pane);
		signin_btn = new Button();
		signup_btn = new Button();
		submit_btn = new Button();
		submit_btn1 = new Button();
		new_db = new DB();
		username = new TextField();
		password = new PasswordField();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Image img = new Image("img/sign-in.jpg");
        ImageView imgView = new ImageView(img);
		imgView.setFitWidth(300);
		imgView.setFitHeight(100);
		
		Image img1 = new Image("img/sign-up.png");
        ImageView imgView1 = new ImageView(img1);
		imgView1.setFitWidth(300);
		imgView1.setFitHeight(100);
		
		Image submit_img = new Image("img/submit.png");
        ImageView submit_imgView = new ImageView(submit_img);
		submit_imgView.setFitWidth(300);
		submit_imgView.setFitHeight(100);
		
		signin_btn.setGraphic(imgView);
		signup_btn.setGraphic(imgView1);
		submit_btn.setGraphic(submit_imgView);
		submit_btn1.setGraphic(submit_imgView);
		
		signin_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				signin();
			}
		});
		
		signup_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				signup();
			}
		});
		
		submit_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				if(username.getText() != "" && password.getText() != "")
				{
					String s1 = username.getText();
					String s2 = password.getText();
					try {
						ArrayList<Player> p = new_db.getAll();
						for(int i = 0 ; i < p.size(); i++)
						{
							if(p.get(i).username.equals(s1) && p.get(i).pass.equals(s2))
							{
								System.out.println("ok");
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		
		submit_btn1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				if(username.getText() != "" && password.getText() != "")
				{
					String s1 = username.getText();
					String s2 = password.getText();
					try {
						new_db.insert(s1, s2, "1", 0);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});

		pane.setTop(signin_btn);
		pane.setBottom(signup_btn);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void signin() {
		pane.setTop(null);
		pane.setBottom(null);
		
		Label username_label = new Label("username: ");
		Label password_label = new Label("password: ");
		
		BorderPane username_pane = new BorderPane();  
		BorderPane password_pane = new BorderPane();
		
		username_pane.setTop(username_label);
		username_pane.setBottom(username);
		
		password_pane.setTop(password_label);
		password_pane.setBottom(password);
		
		BorderPane pp = new BorderPane();
		pp.setTop(username_pane);
		pp.setCenter(password_pane);
		pp.setBottom(submit_btn);
		
		pane.setCenter(pp);
		
	}

	public void signup() {
		
		
		pane.setTop(null);
		pane.setCenter(null);
		pane.setBottom(null);
		
		Label username_label = new Label("username: ");
		Label password_label = new Label("password: ");
		//Label Confirmpassword_label = new Label("Confirm password: ");
		
		//PasswordField Confirmpassword = new PasswordField();
		
		BorderPane username_pane = new BorderPane();  
		BorderPane password_pane = new BorderPane();
		//BorderPane Confirmpassword = new BorderPane();
		
		username_pane.setTop(username_label);
		username_pane.setBottom(username);
		
		password_pane.setTop(password_label);
		password_pane.setBottom(password);
		//password_pane.setBottom(Confirmpassword);
		
		BorderPane pp = new BorderPane();
		pp.setTop(username_pane);
		pp.setCenter(password_pane);
		
		pp.setBottom(submit_btn1);
		pane.setCenter(pp);
		
	}
	
	public static void main(String [] args) {
		Application.launch(args);
	}

}
