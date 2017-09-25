package util;

import java.sql.SQLException;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import entity.Book;
import entity.Reader;

/*
 * 工具类
 * 		1.初始化C3P0连接池
 * 		2.创建DbUtils核心工具类对象
 * 		
 */
public class JdbcUtils {
	
	//1.初始化C3P0连接池
	private static DataSource dataSource;
	static{
		dataSource=new ComboPooledDataSource();
	}
	
	/*
	 * 2.创建DbUtils核心工具类对象
	 */
	public static QueryRunner getQueryRunner()
	{
		//创建QueryRunner对象，传入连接池对象
		//在创建QueryRunner的时候，如果传入了数据源对象，那么在使用QueryRunner对象方法的时候，则不需要传入连接对象
		//会自动从数据源中获取连接
		return new QueryRunner(dataSource);
	}
	
	public static boolean IfBookIdExit(String id){
		boolean right=false;
		String sql="select*from book where id='"+id+"'";
		Book book = null;
		try {
			book = JdbcUtils.getQueryRunner().query(sql, new BeanHandler<Book>(Book.class));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			if(book!=null){
			
				right = true;
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"无法正常读取数据");
		}
		return right;
	}

	public static boolean IfReaderExit(String id) {
		boolean right=false;
		String sql="select*from reader where id='"+id+"'";
		Reader book = null;
		try {
			book = JdbcUtils.getQueryRunner().query(sql, new BeanHandler<Reader>(Reader.class));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try{
			if(book!=null){
			
				right = true;
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"无法正常读取数据");
		}
		return right;
	}
	
	
}
