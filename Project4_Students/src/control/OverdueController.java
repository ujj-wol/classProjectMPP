package control;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.dataaccess.DataAccessFacade;
import model.domain.Book;
import model.domain.BookCheckOut;
import model.domain.BookCopy;
import model.domain.CheckoutRecord;
import model.domain.CheckoutRecordEntry;
import model.domain.LibraryMember;

public class OverdueController implements Initializable {
	private Book book;
	DataAccessFacade data;
	@FXML
	TableView<BookCheckOut> table;
	@FXML
	TableColumn<BookCheckOut, String> isbnCol;
	@FXML
	TableColumn<BookCheckOut, String> titleCol;
	@FXML
	TableColumn<BookCheckOut, String> copyCol;
	@FXML
	TableColumn<BookCheckOut, String> checkoutCol;
	@FXML
	TableColumn<BookCheckOut, String> duedateCol;
	@FXML
	TableColumn<BookCheckOut, String> availableCol;

	@FXML
	TextField search;

	@FXML
	SplitPane window;
	@FXML
	AnchorPane header;
	@FXML
	AnchorPane searchResults;

	int count = 1;

	public void show(Book book) {
//		this.book = book;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/OverduedView.fxml"));
			Scene scene = new Scene(root);
			Stage win2 = new Stage();
			win2.setScene(scene);
			win2.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//
	private ObservableList<BookCheckOut> books;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		book = OverdueHomeController.book;
		books = FXCollections.observableArrayList(getBookCopies());
		// TODO Auto-generated method stub
		isbnCol.setCellValueFactory(new PropertyValueFactory<BookCheckOut, String>("isbn"));
		titleCol.setCellValueFactory(new PropertyValueFactory<BookCheckOut, String>("title"));
		copyCol.setCellValueFactory(new PropertyValueFactory<BookCheckOut, String>("copyNumber"));
		checkoutCol.setCellValueFactory(new PropertyValueFactory<BookCheckOut, String>("memberName"));
		duedateCol.setCellValueFactory(new PropertyValueFactory<BookCheckOut, String>("duedate"));
		availableCol.setCellValueFactory(new PropertyValueFactory<BookCheckOut, String>("availability"));

		table.setItems(books);
	}

	// Show the Checkout list view
	public List<BookCheckOut> getBookCopies() {
		List<BookCheckOut> list = new ArrayList<>();

		BookCopy[] copies = book.getCopies();

		data = new DataAccessFacade();
		// get all the Books in the library
		HashMap<String, LibraryMember> members = data.readMemberMap();

		if (copies == null)
			return list;

		BookCheckOut copyTable;
		CheckoutRecord record;
		List<CheckoutRecordEntry> entries;
		BookCopy checkoutCopy;
		// Columns
		String isbn, title, copyNumber, memberName, duedate, availability;
		isbn = book.getIsbn();
		title = book.getTitle();
		for (int i = 0; i < copies.length; i++) {
			copyNumber = copies[i].getCopyNum() + "";
			memberName = "-";
			duedate = "-";

			availability = "Available";

			List<LibraryMember> libMembers = new ArrayList<>();

			if (members != null) {
				for (String key : members.keySet()) {
					libMembers.add(members.get(key));
				}
			}

			// Foreach member
			for (LibraryMember lib : libMembers) {
				// Get his/her checkout record
				record = lib.getCheckoutRecord();

				if (record != null) {
					// Get the list of record entries for that record
					entries = record.getCheckoutEntries();

					// For each entry get the BookCopy and compare if it is a copy of this book we
					// are on!
					for (CheckoutRecordEntry entry : entries) {
						checkoutCopy = entry.getBookCopy();
						if (checkoutCopy.equals(copies[i])) {
							// Get member name (if any) and duedate
							memberName = lib.getFirstName() + " " + lib.getLastName();
							duedate = entry.getDueDate().toString();
							if (entry.getDueDate().isBefore(LocalDate.now())) {
								availability = "Overdued";
							} else {
								availability = "Checked out";
							}
						}

					}
				}
			}

			copyTable = new BookCheckOut(isbn, title, copyNumber, memberName, duedate, availability);
			list.add(copyTable);
		}

		return list;
	}
	
	// Show the Checkout list view
	public List<BookCheckOut> getBookCopies(String input) {
		
		if(input.isEmpty()) return getBookCopies();
		
		List<BookCheckOut> list = new ArrayList<>();
		
		BookCopy[] copies = book.getCopies();
		
		data = new DataAccessFacade();
		// get all the Books in the library
		HashMap<String, LibraryMember> members = data.readMemberMap();
		
		if (copies == null)
			return list;
		
		BookCheckOut copyTable;
		CheckoutRecord record;
		List<CheckoutRecordEntry> entries;
		BookCopy checkoutCopy;
		// Columns
		String isbn, title, copyNumber, memberName, duedate, availability;
		isbn = book.getIsbn();
		title = book.getTitle();
		for (int i = 0; i < copies.length; i++) {
			copyNumber = copies[i].getCopyNum() + "";
			memberName = "-";
			duedate = "-";
			
			availability = "Available";
			
			List<LibraryMember> libMembers = new ArrayList<>();
			
			if (members != null) {
				for (String key : members.keySet()) {
					libMembers.add(members.get(key));
				}
			}
			
			// Foreach member
			for (LibraryMember lib : libMembers) {
				// Get his/her checkout record
				record = lib.getCheckoutRecord();
				
				if (record != null) {
					// Get the list of record entries for that record
					entries = record.getCheckoutEntries();
					
					// For each entry get the BookCopy and compare if it is a copy of this book we
					// are on!
					for (CheckoutRecordEntry entry : entries) {
						checkoutCopy = entry.getBookCopy();
						if (checkoutCopy.equals(copies[i])) {
							// Get member name (if any) and duedate
							memberName = lib.getFirstName() + " " + lib.getLastName();
							duedate = entry.getDueDate().toString();
							if (entry.getDueDate().isBefore(LocalDate.now())) {
								availability = "Overdued";
							} else {
								availability = "Checked out";
							}
						}
						
					}
				}
			}
			if(availability.equalsIgnoreCase(input)) {
				copyTable = new BookCheckOut(isbn, title, copyNumber, memberName, duedate, availability);
				list.add(copyTable);
			}
			
		}
		
		return list;
	}
	
	@FXML
	private void search() {
		String input = search.getText();
		books = FXCollections.observableArrayList(getBookCopies(input));
		table.setItems(books);
	}
}
