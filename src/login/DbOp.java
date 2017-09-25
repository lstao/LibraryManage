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
	//1.��ʼ��C3P0���ӳ�
	private static Connection con;
	private static DataSource dataSource;
	static{
		try {
			dataSource=new ComboPooledDataSource();
			con=dataSource.getConnection();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"���ݿ�δ�ܴ򿪣�");
			System.out.println(e.getMessage());
		}
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
	
	public static ResultSet executeQuery(String sql){
		ResultSet rs=null;
		try{
			Statement st=con.createStatement();
			rs=st.executeQuery(sql);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"���ݲ�ѯʧ�ܣ�");
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
			JOptionPane.showMessageDialog(null,"���ݿ����ʧ�ܣ�");			
			a= -1;
		}
		return a;
	}
	public static void close(){
		try{
			if(con!=null){
				
				con.close();
				con=null;
				//JOptionPane.showMessageDialog(null,"���ݿ��ѹرգ�");
			}
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,"���ݿ�δ�򿪣�");
		}
	}
}