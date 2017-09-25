package entity;
//Book.java;
//package PublicModule;
import java.sql.Date;
public class Book {
	private  String id;
	private  String bookname;
	private  String booktype;
	private  String author;
	private  String translator;
	private String publisher;
	private  Date publish_time;
	private  int stock,page;
	private  float price;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public String getBooktype() {
		return booktype;
	}
	public void setBooktype(String booktype) {
		this.booktype = booktype;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTranslator() {
		return translator;
	}
	public void setTranslator(String translator) {
		this.translator = translator;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public Date getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(Date publish_time) {
		this.publish_time = publish_time;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String[] getBookinformation() {
		String[] str={id,bookname,String.valueOf(page),author,translator,publisher,publish_time.toString(),String.valueOf(price),String.valueOf(stock)};
		return str;
	}
	@Override
	public String toString() {
		return getBookinformation().toString();
	}
}