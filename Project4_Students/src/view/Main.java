package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
			//Parent root = FXMLLoader.load(LibrarianController.class.getResource("LibrarianPage.fxml"));
			// create a scene with root in it
			Scene scene = new Scene(root);

			// set scene onto the stage
			primaryStage.setScene(scene);
			primaryStage.setTitle("Library Application");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
