package login;
//package PublicModule;
import javax.swing.JOptionPane;

import org.apache.commons.dbutils.handlers.BeanHandler;

import entity.Book;
import util.JdbcUtils;
public class BookSelect{
	public static Book SelectBookByID(String id){
		try{
		String sql="select * from book where id ='"+id+"'";
			return JdbcUtils.getQueryRunner().query(sql, new BeanHandler<Book>(Book.class));
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"无法正常读取数据库！"+e.getMessage());
			throw new RuntimeException();
		}
	}
}