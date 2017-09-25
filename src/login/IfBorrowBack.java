package login;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.commons.dbutils.handlers.BeanHandler;

import entity.Borrower;
import util.JdbcUtils;
public class IfBorrowBack{
	public static Borrower findBook(String bookid,String readerid){
		String sql;
		sql="select * from borrow where book_id='"+bookid+"' and reader_id='"+readerid+"' and if_back='·ñ'";
		Borrower borrow = null;
		try {
			return  JdbcUtils.getQueryRunner().query(sql, new BeanHandler<Borrower>(Borrower.class));
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new RuntimeException();
		}
	}
}