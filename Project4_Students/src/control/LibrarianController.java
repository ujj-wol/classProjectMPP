package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//public class LibrarianController {
//
//	@FXML
//	public void addMember(ActionEvent event) {
//		try {
//			((Node) (event.getSource())).getScene().getWindow().hide();
//
//			Parent root = FXMLLoader.load(getClass().getResource("/view/AddLibraryMember.fxml"));
//			// create a scene with root in it
//			Scene scene = new Scene(root);
//
//			// get stage
//			Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
//
//			// set scene onto the stage
//			primaryStage.setScene(scene);
//			primaryStage.show();
//
//		} catch (Exception e) {
//			System.out.println("Ball");
//		}
//	}
//
//	@FXML
//	public void logOut(ActionEvent event) {
//
//	}
//}





//package control;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.dataaccess.DataAccess;
import model.dataaccess.DataAccessFacade;
import model.domain.Book;
import model.domain.BookCopy;
import model.domain.CheckoutRecord;
import model.domain.CheckoutRecordEntry;
import model.domain.LibraryMember;

public class LibrarianController {
	@FXML
	private Label lblMessage;
	@FXML
	private TextField memberID;
	@FXML
	private TextField isbnNumber;

	@FXML
	private TableView<CheckoutRecordEntry> tableView;

	@FXML
	private TableColumn<CheckoutRecordEntry, String> isbn;

	@FXML
	private TableColumn<CheckoutRecordEntry, String> bookTitle;

	@FXML
	private TableColumn<CheckoutRecordEntry, LocalDate> coDate;

	@FXML
	private TableColumn<CheckoutRecordEntry, LocalDate> dDate;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		String memID = memberID.getText();
		String isbnNum = isbnNumber.getText();
		this.checkout(memID, isbnNum);
	}

	@FXML
	protected void handleSearchButtonAction(ActionEvent event) {
		
		DataAccess da = new DataAccessFacade();
		HashMap<String, LibraryMember> libraryMembers = da.readMemberMap();

		LibraryMember member = libraryMembers.get(memberID.getText());
		if (member != null) {
			CheckoutRecord checkoutRecord = member.getCheckoutRecord();
			if (checkoutRecord != null) {
				this.displayTableView(checkoutRecord);
			} else {
				canNotFind("Can not find any checkout record!");
			}
		} else {
			canNotFind("Can not find this member id");
			tableView.setItems(null);
		}
	}
	
	public void displayTableView(CheckoutRecord checkoutRecord) {
		List<CheckoutRecordEntry> recordEntries = checkoutRecord.getCheckoutEntries();
		
		// Display to console 
		for (CheckoutRecordEntry recordEntry: recordEntries) {
			System.out.println("ISBN: " + recordEntry.getBookCopy().getBook().getIsbn());
			System.out.println("Title: " + recordEntry.getBookCopy().getBook().getTitle());
			System.out.println("Checkout Date: " + recordEntry.getCheckoutDate());
			System.out.println("Due Date: " + recordEntry.getDueDate());
			System.out.println("================================");
		}

		final ObservableList<CheckoutRecordEntry> data = FXCollections.observableArrayList(recordEntries);
		isbn.setCellValueFactory(
				new Callback<CellDataFeatures<CheckoutRecordEntry, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<CheckoutRecordEntry, String> p) {
						String isbnString = p.getValue().getBookCopy().getBook().getIsbn();
						return new SimpleStringProperty(isbnString);
					}
				});

		bookTitle.setCellValueFactory(
				new Callback<CellDataFeatures<CheckoutRecordEntry, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<CheckoutRecordEntry, String> p) {
						String bookTitleString = p.getValue().getBookCopy().getBook().getTitle();
						return new SimpleStringProperty(bookTitleString);
					}
				});
		coDate.setCellValueFactory(new PropertyValueFactory<CheckoutRecordEntry, LocalDate>("checkoutDate"));
		dDate.setCellValueFactory(new PropertyValueFactory<CheckoutRecordEntry, LocalDate>("dueDate"));

		tableView.setItems(data);
	}

	public void checkout(String memID, String isbnNum) {

		DataAccess da = new DataAccessFacade();
		HashMap<String, LibraryMember> libraryMembers = da.readMemberMap();

		LibraryMember member = libraryMembers.get(memID);
		if (member != null) {
			HashMap<String, Book> books = da.readBooksMap();
			Book book = books.get(isbnNum);
			if (book != null) {
				if (book.getCopyNums() == null) {
					canNotFind("Can not find copy of this book!");
				} else {
					if (member != null && book != null) {
						boolean bookAvailable = true;
						if (!book.isAvailable()) {
							canNotFind("This book is not available!");
							bookAvailable = false;
						}
						boolean copyAvailable = false;
						if (bookAvailable) {
							BookCopy[] bookCopies = book.getCopies();
							BookCopy bookCopy = null;
							for (BookCopy bCopy : bookCopies) {
								if (bCopy.isAvailable()) {
									copyAvailable = true;
									bookCopy = bCopy;
									break;
								}
							}

							if (copyAvailable) {
								LocalDate checkoutDate = LocalDate.now();
								LocalDate dueDate = checkoutDate.plusDays(book.getMaxCheckoutLength());
								// If both member ID and book ID are found and a copy is available, a new
								// checkout record entry is created
								CheckoutRecordEntry cRecordEntry = new CheckoutRecordEntry(checkoutDate, dueDate,
										bookCopy);
								CheckoutRecord checkoutRecord = null;
								if (member.getCheckoutRecord() != null) {
									checkoutRecord = member.getCheckoutRecord();
								} else {
									checkoutRecord = new CheckoutRecord();
								}

								// This checkout entry is then added to the member’s checkout record
								checkoutRecord.addRecordEntry(cRecordEntry);
								member.setCheckoutRecord(checkoutRecord);
								da.saveNewMember(member);

								// The copy that is checked out is marked as unavailable.
								bookCopy.changeAvailability();
																
								this.displayTableView(checkoutRecord);
								success("Checkout successfully!");
							}
						}
					}

				}
			} else {
				canNotFind("Can not find this book!");
			}

		} else {
			canNotFind("Can not find this member!");
			tableView.setItems(null);
		}

	}
	
	@FXML
	private Text msg;
	
	public void canNotFind(String errorMsg) {
		msg.setText(errorMsg);
		msg.setFill(Paint.valueOf("red"));
	}
	
	public void success(String successMsg) {
		msg.setText(successMsg);
		msg.setFill(Paint.valueOf("green"));
	}
	
	@FXML
	private void clearMessage() {
		msg.setText("");
	}

	@FXML
	public void addMember(ActionEvent event) {
		try {
			((Node) (event.getSource())).getScene().getWindow().hide();

			Parent root = FXMLLoader.load(getClass().getResource("/view/AddLibraryMember.fxml"));
			// create a scene with root in it
			Scene scene = new Scene(root);

			// get stage
			Stage primaryStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

			// set scene onto the stage
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			System.out.println("Ball");
		}
	}

	@FXML
	public void logOut(ActionEvent event) {

	}
	
}


