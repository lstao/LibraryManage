package login;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.dbutils.ResultSetHandler;
import org.junit.Test;

import util.JdbcUtils;
import annotation.Clazs;
import entity.Book;
import entity.Borrower;
import entity.BorrowerInfo;
import entity.Reader;

public class BorrowReturn {
	@Test
	public static List<BorrowerInfo> getBorrwers(String sql) {
	
		/*String sql="SELECT borrow.id id,borrow_date,back_date,if_back, reader.id reader_id,book.id book_id"
				+ ",readername,readertype,max_num,days_num,"+ "bookname,author,publisher,publish_time,price,stock "
				+ "FROM borrow INNER JOIN  reader ON borrow.reader_id=reader.id INNER JOIN book ON borrow.book_id =book.id";*/
		List<BorrowerInfo> list;
		
		try {
			return list = JdbcUtils.getQueryRunner().query(sql, new MyBeanListHandler<BorrowerInfo>(BorrowerInfo.class));
//			for(BorrowerInfo b:list){
//				System.out.println(b.getBook().toString());
//				System.out.println(b.getReader().toString());
//				System.out.println(b.getBorrower().toString());
//				System.out.println("------------------------------------------------------------");
//			}
//			System.out.println("���������������в���Ҫ���ֶ����ַ�װ��ʱ��trycatch��continue��");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
}


class MyBeanListHandler<T> implements ResultSetHandler<List<T>>{
	//Ҫ��װ������ֽ���
	private Class<T> clazz;
	public MyBeanListHandler(Class<T> clazz){
		this.clazz=clazz;
	}
	
	
	
	
	
	 static {
		 ConvertUtils.register(new Converter() {

			@Override
			public Object convert(Class arg0, Object arg1) {
				if(arg1==null){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	                try {
	                	System.out.println("��������");
						return df.parse(df.format(new Date()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					 return arg1;
					 }
			}
			 , Date.class); 
		 
		  //ע��util.date��ת������������BeanUtils.copyPropertiesʱ��ԴĿ���util���͵�ֵ����Ϊ��
		// ConvertUtils.register(new DateConverter(), java.util.Date.class);
		//ConvertUtils.register(new SqlTimestampConverter(), java.sql.Timestamp.class);
				}
	//���ݿ��ѯ��ÿһ�м�¼����װΪһ����������ӵ�list���ϣ�����list
	@Override
	public   List<T> handle(ResultSet rs) throws SQLException {
		List<T> list=new ArrayList<T>();
		try {
			
			
			//���¶�һ��
			while(rs.next())
			{
				//����Ҫ��װ�Ķ���
				T t=clazz.newInstance();
				//a.��ȡ����Field�ֶ�����
				Field[] fs=clazz.getDeclaredFields();
				//b.��������ȡÿһ���ֶ����ͣ�Field
				for(Field f:fs)
				{
					f.setAccessible(true);
					//c.��ȡ��������
					String fieldName=f.getName();
					//��ȡ��ʳ�ϵ���ȫ�������䣬Ȼ��ʵ����Ϊ����
					Clazs clazs=f.getAnnotation(Clazs.class);
					String forName=clazs.value();
					Class clazz2= Class.forName(forName);
					Object k=(Object)clazz2.newInstance();
					//ͨ�����ֻ�ȡid
					String test=forName.substring(forName.lastIndexOf(".")+1);
					if("Book".equals(test)){
						k=(Book)k;
					}else if("Reader".equals(test))
					{
						k=(Reader)k;
					}else if("Borrower".equals(test)){
						k=(Borrower)k;
					}
					//��ȡ�ö��������࣬�����з�װ����
					Field[] fs2=clazz2.getDeclaredFields();
					for(Field f2:fs2){
						f2.setAccessible(true);
						String fieldName2=f2.getName();
						String bak=fieldName2;
						if("Book".equals(test)){
							if("id".equals(fieldName2)){
								fieldName2="book_id";
								
							}
						}else if("Reader".equals(test)){
							if("id".equals(fieldName2)){
								fieldName2="reader_id";
							}
						}
						
						Object Value=null;
						try {
							Value = rs.getObject(fieldName2);
						} catch (Exception e) {
						
								continue;
						}
						
						BeanUtils.copyProperty(k,bak,Value);
					}
					//e.��ȡField�ֶ���ע��
					//Column column=f.getAnnotation(Column.class);
					//f.�ֶ���
					//String columnName=column.columnName();
					//g.�ֶ�ֵ
					//Object columnValue=rs.getObject(fieldName);
					
					
					//���ã�BeanUtils)���������֮��
					BeanUtils.copyProperty(t, fieldName, k);
				
					//��ӵ�list
					
				}
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} 
		
		
	}
}