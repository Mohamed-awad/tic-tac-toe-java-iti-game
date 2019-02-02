package gui_server;
import server.server;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Gui_server extends Application{

	server server1;
	
	public Gui_server() {
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		Image img = new Image("img/on.jpg");
        ImageView imgView = new ImageView(img);
		imgView.setFitWidth(300);
		imgView.setFitHeight(100);
		
		Image img1 = new Image("img/off.jpg");
        ImageView imgView1 = new ImageView(img1);
		imgView1.setFitWidth(300);
		imgView1.setFitHeight(100);
		
		Button start_btn = new Button();
		Button stop_btn = new Button();
		
		start_btn.setGraphic(imgView);
		stop_btn.setGraphic(imgView1);
		
		start_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				server1 = new server();
				server1.start();
				pane.setCenter(stop_btn);
			}
		});
		
		stop_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				server1.stop();
				server1 = null;
				pane.setCenter(start_btn);
			}
		});

		pane.setCenter(start_btn);
				
		Scene scene = new Scene(pane, 400, 200);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String [] args) {
		Application.launch(args);
	}

}
