package model.dataaccess;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import model.domain.Address;
import model.domain.Author;
import model.domain.Book;
import model.domain.LibraryMember;
import model.domain.User;


public class DataAccessFacade implements DataAccess {

	enum StorageType {
		BOOKS, MEMBERS, USERS, AUTHORS, ADDRESSES;
	}

	public static final String OUTPUT_DIR = System.getProperty("user.dir")
			+ "//src//model//dataaccess//storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";

	//implement: other save operations
	public void saveNewMember(LibraryMember member) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		String memberId = member.getMemberId();
		mems.put(memberId, member);
		saveToStorage(StorageType.MEMBERS, mems);
	}
	
	public void saveNewBook(Book book) {
		HashMap<String, Book> books = readBooksMap();
		String isbn = book.getIsbn();
		books.put(isbn, book);
		saveToStorage(StorageType.BOOKS, books);
	}
	
	public void saveNewAuthor(Author author) {
		HashMap<String, Author> authors = readAuthorMap();
		String fname = author.getFirstName();
		authors.put(fname, author);
		saveToStorage(StorageType.AUTHORS, authors);
	}

	@SuppressWarnings("unchecked")
	public  HashMap<String,Book> readBooksMap() {
		//Returns a Map with name/value pairs being
		//   isbn -> Book
		return (HashMap<String,Book>) readFromStorage(StorageType.BOOKS);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, LibraryMember> readMemberMap() {
		//Returns a Map with name/value pairs being
		//   memberId -> LibraryMember
		return (HashMap<String, LibraryMember>) readFromStorage(
				StorageType.MEMBERS);
	}


	@SuppressWarnings("unchecked")
	public HashMap<String, User> readUserMap() {
		//Returns a Map with name/value pairs being
		//   userId -> User
		return (HashMap<String, User>)readFromStorage(StorageType.USERS);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Author> readAuthorMap() {
		//Returns a Map with name/value pairs being
		//   fName -> Author
		return (HashMap<String, Author>)readFromStorage(StorageType.AUTHORS);
	}


	/////load methods - these place test data into the storage area
	///// - used just once at startup
	//static void loadMemberMap(List<LibraryMember> memberList) {

	static void loadBookMap(List<Book> bookList) {
		HashMap<String, Book> books = new HashMap<String, Book>();
		bookList.forEach(book -> books.put(book.getIsbn(), book));
		saveToStorage(StorageType.BOOKS, books);
	}
	static void loadUserMap(List<User> userList) {
		HashMap<String, User> users = new HashMap<String, User>();
		userList.forEach(user -> users.put(user.getId(), user));
		saveToStorage(StorageType.USERS, users);
	}

	static void loadMemberMap(List<LibraryMember> memberList) {
		HashMap<String, LibraryMember> members = new HashMap<String, LibraryMember>();
		memberList.forEach(member -> members.put(member.getMemberId(), member));
		saveToStorage(StorageType.MEMBERS, members);
	}
	
	static void loadAuthorMap(List<Author> authorList) {
		HashMap<String, Author> authors = new HashMap<>();
		authorList.forEach(author -> authors.put(author.getFirstName(), author));
		saveToStorage(StorageType.AUTHORS, authors);
	}

	static void saveToStorage(StorageType type, Object ob) {
		ObjectOutputStream out = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			out = new ObjectOutputStream(Files.newOutputStream(path));
			out.writeObject(ob);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception e) {}
			}
		}
	}

	static Object readFromStorage(StorageType type) {
		ObjectInputStream in = null;
		Object retVal = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			in = new ObjectInputStream(Files.newInputStream(path));
			retVal = in.readObject();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch(Exception e) {}
			}
		}
		return retVal;
	}

	public void saveNewAddress(Address address) {
		// TODO Auto-generated method stub
		HashMap<String, Address> addresses = readAddressMap();
		String street = address.getStreet();
		addresses.put(street, address);
		saveToStorage(StorageType.ADDRESSES, addresses);
		
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Address> readAddressMap() {
		//Returns a Map with name/value pairs being
		//	street -> address
		return (HashMap<String, Address>)readFromStorage(StorageType.ADDRESSES);
	}
	
	static void loadAddressMap(List<Address> addressList) {
		// TODO Auto-generated method stub
		HashMap<String, Address> addresses = new HashMap<>();
		addressList.forEach(address -> addresses.put(address.getStreet(), address));
		saveToStorage(StorageType.ADDRESSES, addresses);
	}
}
