package control;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dataaccess.DataAccessFacade;
import model.domain.Address;
import model.domain.LibraryMember;

public class AddLibraryMemberController {
	@FXML
	private TextField memberId;
	@FXML
	private TextField fName;
	@FXML
	private TextField lName;
	@FXML
	private TextField street;
	@FXML
	private TextField city;
	@FXML
	private TextField state;
	@FXML
	private TextField zip;
	@FXML
	private TextField phone;
	@FXML
	private Address address;
	@FXML
	private LibraryMember newPerson;

	@FXML
	public void save(ActionEvent event) {
		try {
			((Node) (event.getSource())).getScene().getWindow().hide();

			Parent root = FXMLLoader.load(getClass().getResource("/view/LibrarianPage.fxml"));
			// create a scene with root in it
			Scene scene = new Scene(root);

			// get stage
			Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

			// set scene onto the stage
			primaryStage.setScene(scene);
			primaryStage.show();

			address = new Address(street.getText(), city.getText(), state.getText(), zip.getText());
			newPerson = new LibraryMember(memberId.getText(), fName.getText(), lName.getText(), phone.getText(),
					address);
			
			// save the member now to the database
			DataAccessFacade df = new DataAccessFacade();
			df.saveNewMember(newPerson);
			System.out.println(df.readMemberMap());
			
			// save these new addresses to the database
			df.saveNewAddress(address);
			System.out.println(df.readAddressMap());

		} catch (Exception e) {
			System.out.println("Cat");
		}
	}

	@FXML
	public void cancel(ActionEvent event) throws IOException {
		((Node) (event.getSource())).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/view/LibrarianPage.fxml"));
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
