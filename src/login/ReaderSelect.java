package login;
//package PublicModule;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.commons.dbutils.handlers.BeanHandler;

import entity.Reader;
import util.JdbcUtils;
public class ReaderSelect{
	public static Reader SelectReaderByID(String id){
		try{
			String sql="select * from reader where id='"+id+"'";
			return JdbcUtils.getQueryRunner().query(sql, new BeanHandler<Reader>(Reader.class));
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"无法正常读取数据库！");
			throw new RuntimeException();
		}
	}
}