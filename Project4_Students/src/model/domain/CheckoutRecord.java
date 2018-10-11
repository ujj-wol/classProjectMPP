package model.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckoutRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CheckoutRecordEntry> checkoutEntries; 
	public void addRecordEntry(CheckoutRecordEntry cRecordEntry) {
		checkoutEntries.add(cRecordEntry);
	}
	public CheckoutRecord() {
		checkoutEntries = new ArrayList<CheckoutRecordEntry>();
	}
	public List<CheckoutRecordEntry> getCheckoutEntries() {
		return checkoutEntries;
	}
	public void setCheckoutEntries(List<CheckoutRecordEntry> checkoutEntries) {
		this.checkoutEntries = checkoutEntries;
	}	
	
	
}
