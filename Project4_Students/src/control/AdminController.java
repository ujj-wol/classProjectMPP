package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminController {

	@FXML
	public void addBook(ActionEvent event) {
		try {
			//((Node)(event.getSource())).getScene().getWindow().hide();

			Parent root = FXMLLoader.load(getClass().getResource("/view/AddBook.fxml"));
			// create a scene with root in it
			Scene scene = new Scene(root);

			// get stage
			Stage primaryStage = (Stage)((Node)(event.getSource())).getScene().getWindow();

			// set scene onto the stage
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			System.out.println("Ball");
		}
	}

	@FXML
	public void logOut(ActionEvent event) {
		System.out.println("Hello");
	}
}
