package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dataaccess.DataAccessFacade;
import model.domain.Address;
import model.domain.Author;
import model.domain.Book;

public class AddBookController {
	@FXML
	private TextField isbn;
	@FXML
	private TextField title;
	@FXML
	private TextField maxCheckoutLength;
	@FXML
	private TextField numCopies;
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
	private static List<Author> authorlist = new ArrayList<>();
	@FXML
	private static List<Address> addresslist = new ArrayList<>();
	@FXML
	private Book book;
	@FXML
	private int numberOfBookCopy;

	@FXML
	public void addBook(ActionEvent event) {
		try {
			
			((Node) (event.getSource())).getScene().getWindow().hide();

			Parent root = FXMLLoader.load(getClass().getResource("/view/AdminPage.fxml"));
			// create a scene with root in it
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.show();

			address = new Address(street.getText(), city.getText(), state.getText(), zip.getText());
			addresslist.add(address);
			anAuthor = new Author(authorFname.getText(), authorLname.getText(), authorPhone.getText(), address, authorBio.getText());
			authorlist.add(anAuthor);
			book = new Book(isbn.getText(), title.getText(), Integer.parseInt(maxCheckoutLength.getText()), authorlist);
			numberOfBookCopy = Integer.parseInt(numCopies.getText());
			addBookCopies();
			
			// save the book now to the database. Also save the authors and their addresses
			DataAccessFacade df = new DataAccessFacade();
			df.saveNewBook(book);
			for(Address a : addresslist) 
				df.saveNewAddress(a);
			for(Author a : authorlist)
				df.saveNewAuthor(a);
			
			// to see if the added data are mirrored to the storage
			System.out.println(df.readBooksMap());
			System.out.println();
			System.out.println(df.readAddressMap());
			System.out.println();
			System.out.println(df.readAuthorMap());
			
			//check if book copies are properly created
			System.out.println(book.getNumCopies());
			System.out.println(Arrays.toString(book.getCopies()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addBookCopies() {
		// TODO Auto-generated method stub
		//one copy is already created when a book is added. Only if there are multiple copies we need to add those copies
		for(int i = 1; i < numberOfBookCopy; i++) {
			book.addCopy();
		}
		
	}

	public void confirmMaxDays(ActionEvent event) {
		try {
			int days = Integer.parseInt(maxCheckoutLength.getText());
			if(days != 7 || days != 21) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, an Error Dialog");
				alert.setContentText("Maximum Checkout Days can only be 7 or 21.");

				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void addAuthor(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/view/AddAuthor.fxml"));
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML
	public void cancel(ActionEvent event) throws IOException {
		((Node) (event.getSource())).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/view/AdminPage.fxml"));
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void addAuthorToAuthorList(Author a) {
		authorlist.add(a);
	}
	
	public static void addAddressToAddressList(Address a) {
		addresslist.add(a);
	}
}
