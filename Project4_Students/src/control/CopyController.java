package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.dataaccess.DataAccessFacade;
import model.domain.Book;

public class CopyController implements Initializable{
	private Book book;

	@FXML
	Text title;
	@FXML
	Text authors;
	@FXML
	Text copies;

	@FXML
	Button addBtn;
	
	@FXML GridPane layout;

	public void show(Book book) {
		this.book = book;

		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/CopyView.fxml"));
			Scene scene = new Scene(root);
			Stage win2 = new Stage();
			win2.setScene(scene);
			win2.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void addCopy() {		
		if (book != null) {
			book.addCopy();
			DataAccessFacade df = new DataAccessFacade();
			df.saveNewBook(book);
			copies.setText(book.getCopyNums().size() + "");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		book = AddBookCopyController.book;

		title.setText(book.getTitle());
		authors.setText(book.getTitle());
		copies.setText(book.getCopies().length + "");
	}

}
