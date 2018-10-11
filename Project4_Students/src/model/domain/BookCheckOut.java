package model.domain;

public class BookCheckOut {
	private String isbn;
	private String title;
	private String copyNumber;
	private String memberName;
	private String duedate;
	private String availability;
	
	public BookCheckOut(String isbn, String title, String copyNumber, String memberName, String duedate, String availability) {
		this.isbn = isbn;
		this.title = title;
		this.copyNumber = copyNumber;
		this.memberName = memberName;
		this.duedate = duedate;
		this.availability = availability;
	}
	
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCopyNumber() {
		return copyNumber;
	}
	public void setCopyNumber(String copyNumber) {
		this.copyNumber = copyNumber;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getDuedate() {
		return duedate;
	}
	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}	

}
