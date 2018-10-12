package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.dataaccess.DataAccessFacade;
import model.domain.Book;

public class AddBookCopyController implements Initializable {
	public static Book book;
	DataAccessFacade data;
	@FXML
	TableView<Book> table;
	@FXML
	TableColumn<Book, String> isbnCol;
	@FXML
	TableColumn<Book, String> titleCol;
	@FXML
	TableColumn<Book, String> authorsCol;
	
	@FXML
	TextField search;

	@FXML
	SplitPane window;
	@FXML
	AnchorPane header;
	@FXML
	AnchorPane searchResults;
	
	private String destination;
	{
		if(LoginController.accessLevel.equals("Admin"))
			destination = "/view/AdminPage.fxml";
		else
			destination = "/view/SuperUser.fxml";
	}

	int count = 1;
	
	private ObservableList<Book> books = FXCollections.observableArrayList(getBooks());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		table.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				Book bk = table.getSelectionModel().getSelectedItem();
				if(bk != null) {
					book = bk;
					CopyController copy = new CopyController();				
					copy.show(bk);	
				}
							
			}
			
		});
		// TODO Auto-generated method stub
		isbnCol.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		authorsCol.setCellValueFactory(new PropertyValueFactory<Book, String>("authorList"));

		table.setItems(books);
	}

	// Show the addBook view
	public List<Book> getBooks() {
		data = new DataAccessFacade();
		List<Book> list = new ArrayList<>();
		// get all the Books in the library
		HashMap<String, Book> books = data.readBooksMap();
		if (books != null) {
			for (String key : books.keySet()) {
				list.add(books.get(key));
			}
		}
		return list;
	}
	
	// Show the addBook view
	public List<Book> getBooks(String isbn) {
		if(isbn.isEmpty()) return getBooks();
		data = new DataAccessFacade();
		List<Book> list = new ArrayList<>();
		// get all the Books in the library
		HashMap<String, Book> books = data.readBooksMap();
		if (books != null) {
			for (String key : books.keySet()) {
				if(books.get(key).getIsbn().equals(isbn)) {
					list.add(books.get(key));
				}				
			}
		}
		return list;
	}

	public void addCopy(Book book) {
		if (book != null) {
			book.addCopy();
		}
	}

	@FXML
	private void search() {
		String input = search.getText();
		books = FXCollections.observableArrayList(getBooks(input));
		table.setItems(books);
	}
	
	@FXML
	public void back(ActionEvent event) throws IOException {
		((Node) (event.getSource())).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource(destination));
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
