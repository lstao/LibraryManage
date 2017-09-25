package login;
//package PublicModule;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;
public class DbOp{
	//1.初始化C3P0连接池
	private static Connection con;
	private static DataSource dataSource;
	static{
		try {
			dataSource=new ComboPooledDataSource();
			con=dataSource.getConnection();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"数据库未能打开！");
			System.out.println(e.getMessage());
		}
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
	
	public static ResultSet executeQuery(String sql){
		ResultSet rs=null;
		try{
			Statement st=con.createStatement();
			rs=st.executeQuery(sql);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"数据查询失败！");
			rs=null;
		}
		return rs;
	}
	public static int executeUpdate(String sql){
			int a=0;
		try{
			if(con==null){
				new DbOp();
			}
			a= con.createStatement().executeUpdate(sql);
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,"数据库更新失败！");			
			a= -1;
		}
		return a;
	}
	public static void close(){
		try{
			if(con!=null){
				
				con.close();
				con=null;
				//JOptionPane.showMessageDialog(null,"数据库已关闭！");
			}
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,"数据库未打开！");
		}
	}
}