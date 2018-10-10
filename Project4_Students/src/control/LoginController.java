package control;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dataaccess.Auth;
import model.dataaccess.DataAccessFacade;
import model.dataaccess.TestData;
import model.domain.User;

public class LoginController {
	@FXML
	private TextField id;
	@FXML
	private PasswordField password;

	private String idString;
	private String passString;

	private DataAccessFacade df = new DataAccessFacade();
	private HashMap<String, User> userMap = df.readUserMap();

	// when this method is called it will change the scene to librarian, admin or
	// both accordingly
	@FXML
	public void submit(ActionEvent event) {
		try {
			idString = id.getText().trim();
			passString = password.getText().trim();

			if (userMap.containsKey(idString)) {
				User user = userMap.get(idString);

				if (user.getPassword().equals(passString)) {
					Auth authorization = user.getAuthorization();

					Parent root;
					// based on the user type either librarianPage or AdminPage or BothPage should
					// load

					switch (authorization) {

					case ADMIN:
						root = FXMLLoader.load(AdminController.class.getResource("/view/AdminPage.fxml"));
						break;

					case BOTH:
						root = FXMLLoader.load(AdminController.class.getResource("/view/LibrarianPage.fxml"));
						break;
					
					default:
					case LIBRARIAN:
						root = FXMLLoader.load(LibrarianController.class.getResource("/view/LibrarianPage.fxml"));
						break;
					}

					Scene newScene = new Scene(root);

					// this line gets the stage information
					Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
					window.setScene(newScene);
					window.show();
				} else {
					System.out.println("id and password doesnot match!");
				}
			} else {
				System.out.println("id not found!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
