package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.domain.Address;
import model.domain.Author;
import model.domain.Book;

public class AddAuthorController {
	@FXML
	private TextField authorFname;
	@FXML
	private TextField authorLname;
	@FXML
	private TextField authorPhone;
	@FXML
	private TextArea authorBio;
	@FXML
	private TextField street;
	@FXML
	private TextField city;
	@FXML
	private TextField state;
	@FXML
	private TextField zip;
	@FXML
	private Address address;
	@FXML
	private Author anAuthor;
	@FXML
	private List<Author> authorlist = new ArrayList<>();
	@FXML
	private Book book;

	@FXML
	public void submitAuthor(ActionEvent event) {
		try {
			((Node) (event.getSource())).getScene().getWindow().hide();

//			Parent root = FXMLLoader.load(getClass().getResource("/view/AddBook.fxml"));
//			// create a scene with root in it
//			Scene scene = new Scene(root);
//			Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
//			primaryStage.setScene(scene);
//			primaryStage.show();

			address = new Address(street.getText(), city.getText(), state.getText(), zip.getText());
			anAuthor = new Author(authorFname.getText(), authorLname.getText(), authorPhone.getText(), address, authorBio.getText());
			AddBookController.addAuthorToAuthorList(anAuthor);
			AddBookController.addAddressToAddressList(address);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void cancelAuthor(ActionEvent event) throws IOException {
		((Node) (event.getSource())).getScene().getWindow().hide();
	}
}
