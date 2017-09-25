package entity;

import java.util.Date;

import annotation.Clazs;

public class BorrowerInfo {
	
	@Clazs("entity.Book")
	private Book book;
	@Clazs("entity.Reader")
	private Reader reader;
	@Clazs("entity.Borrower")
	private Borrower borrower;
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Reader getReader() {
		return reader;
	}
	public void setReader(Reader reader) {
		this.reader = reader;
	}
	public Borrower getBorrower() {
		return borrower;
	}
	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}
	
	
	
	
	
}
