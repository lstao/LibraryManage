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
 * ������
 * 		1.��ʼ��C3P0���ӳ�
 * 		2.����DbUtils���Ĺ��������
 * 		
 */
public class JdbcUtils {
	
	//1.��ʼ��C3P0���ӳ�
	private static DataSource dataSource;
	static{
		dataSource=new ComboPooledDataSource();
	}
	
	/*
	 * 2.����DbUtils���Ĺ��������
	 */
	public static QueryRunner getQueryRunner()
	{
		//����QueryRunner���󣬴������ӳض���
		//�ڴ���QueryRunner��ʱ���������������Դ������ô��ʹ��QueryRunner���󷽷���ʱ������Ҫ�������Ӷ���
		//���Զ�������Դ�л�ȡ����
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
			JOptionPane.showMessageDialog(null,"�޷�������ȡ����");
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
			JOptionPane.showMessageDialog(null,"�޷�������ȡ����");
		}
		return right;
	}
	
	
}
