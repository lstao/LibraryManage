package entity;

import java.util.Date;

public class Borrower {
	private String id;//–Ú∫≈
	private String reader_id;//∂¡’ﬂid
	private String book_id;//Õº È±‡∫≈
	private Date borrow_date;
	private Date back_date;
	private String if_back;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReader_id() {
		return reader_id;
	}
	public void setReader_id(String reader_id) {
		this.reader_id = reader_id;
	}
	public String getBook_id() {
		return book_id;
	}
	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}
	public Date getBorrow_date() {
		return borrow_date;
	}
	public void setBorrow_date(Date borrow_date) {
		this.borrow_date = borrow_date;
	}
	public Date getBack_date() {
		return back_date;
	}
	public void setBack_date(Date back_date) {
		this.back_date = back_date;
	}
	public String getIf_back() {
		return if_back;
	}
	public void setIf_back(String if_back) {
		this.if_back = if_back;
	}
	@Override
	public String toString() {
		return id+" "+reader_id+" "+book_id+" "+borrow_date+" "+back_date+" "+if_back;
	}
}
