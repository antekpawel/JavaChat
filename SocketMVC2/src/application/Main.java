package application;
	
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	public static final String appName = "Sockets-JavaFX-MVC";

	
	@Override
	public void start(Stage primaryStage) {
		try {
			ViewLoader<AnchorPane, ChatController> viewLoader = new ViewLoader<>("Sample.fxml");
			viewLoader.getController().setUserName(getUserName());
			viewLoader.getController().setHost("localhost");
			viewLoader.getController().setPort(9001);
			viewLoader.getController().run();
			Scene scene = new Scene(viewLoader.getLayout());
			primaryStage.setScene(scene);
			primaryStage.setTitle(appName);
			primaryStage.setOnHiding( e -> primaryStage_Hiding(e, viewLoader.getController()));
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void primaryStage_Hiding(WindowEvent e, ChatController controller) {
		try {
			controller.close();
		} catch (IOException e1) {
			System.out.print("Error primaryStage_Hiding");
		}
	}

	private String getUserName() {
		TextInputDialog textInputDialog = new TextInputDialog("Anonymous");
		textInputDialog.setTitle("Add user");
		textInputDialog.setHeaderText("Welcome!");
		textInputDialog.setContentText("Set your username:");
		Optional<String> result = textInputDialog.showAndWait();
		return result.orElse("Anonymous");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
