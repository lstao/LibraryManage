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
//			System.out.println("这里我屏蔽了所有不需要的字段名字封装的时候，trycatch，continue了");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
}


class MyBeanListHandler<T> implements ResultSetHandler<List<T>>{
	//要封装的类的字节码
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
	                	System.out.println("经过这里");
						return df.parse(df.format(new Date()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					 return arg1;
					 }
			}
			 , Date.class); 
		 
		  //注册util.date的转换器，即允许BeanUtils.copyProperties时的源目标的util类型的值允许为空
		// ConvertUtils.register(new DateConverter(), java.util.Date.class);
		//ConvertUtils.register(new SqlTimestampConverter(), java.sql.Timestamp.class);
				}
	//数据库查询的每一行记录，封装为一个对象，再添加到list集合，返回list
	@Override
	public   List<T> handle(ResultSet rs) throws SQLException {
		List<T> list=new ArrayList<T>();
		try {
			
			
			//向下读一行
			while(rs.next())
			{
				//创建要封装的对象
				T t=clazz.newInstance();
				//a.获取所有Field字段数组
				Field[] fs=clazz.getDeclaredFields();
				//b.遍历，获取每一个字段类型，Field
				for(Field f:fs)
				{
					f.setAccessible(true);
					//c.获取属性名称
					String fieldName=f.getName();
					//获取主食上的类全名，反射，然后实例化为对象
					Clazs clazs=f.getAnnotation(Clazs.class);
					String forName=clazs.value();
					Class clazz2= Class.forName(forName);
					Object k=(Object)clazz2.newInstance();
					//通过名字获取id
					String test=forName.substring(forName.lastIndexOf(".")+1);
					if("Book".equals(test)){
						k=(Book)k;
					}else if("Reader".equals(test))
					{
						k=(Reader)k;
					}else if("Borrower".equals(test)){
						k=(Borrower)k;
					}
					//获取该对象所有类，并进行封装数据
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
					//e.获取Field字段上注释
					//Column column=f.getAnnotation(Column.class);
					//f.字段名
					//String columnName=column.columnName();
					//g.字段值
					//Object columnValue=rs.getObject(fieldName);
					
					
					//设置（BeanUtils)，加入对象之中
					BeanUtils.copyProperty(t, fieldName, k);
				
					//添加到list
					
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