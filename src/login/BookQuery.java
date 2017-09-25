package login;
import java.awt.Button;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import entity.Book;
import util.JdbcUtils;
public class BookQuery extends JFrame{
	JTable table;
	JScrollPane scrollpane;// ����һ����ʾָ��������ݵ� JScrollPane��ֻҪ��������ݳ�����ͼ��С�ͻ���ʾˮƽ�ʹ�ֱ��������
	String[] lbsign={"ͼ������","��       �� ","��  ��  ��","����ʱ��"};
	Label[] booklb=new Label[4];
	TextField[] booktxt=new TextField[4];
	Button querybtn=new Button("��ѯ");
	Button closebtn=new Button("�ر�");
	String[]heads={"ͼ����","ͼ������","ͼ������","����","����","������","����ʱ��","����","�������","ͼ��ӡ��"};
	Object[][] bookq=new Object[40][heads.length];
	public BookQuery(){
		setTitle("ͼ���ѯ");
		setLayout(null);
		setSize(1200,700);
		/*
		 * ����Ϊboolean���ͣ�resizeableֵΪtrueʱ����ʾ�����ɵĴ���������ɸı��С��
		resizeableֵΪfalseʱ����ʾ���ɵĴ����С���ɳ���Ա�����ģ��û�����������.
		 */
		setResizable(false);
		
		int lx=150;
		for(int i=0;i<booklb.length;i++){
			booklb[i]=new Label(lbsign[i]);
			booktxt[i]=new TextField();
			booklb[i].setBounds(lx,30,50,20);
			booktxt[i].setBounds(lx+70,30,120,20);
			lx=lx+210;
			add(booklb[i]);add(booktxt[i]);
		}
		/*
		 * ǰ�����ֱ�Ϊx��y�����꣬�������ֱ��ǳ��Ⱥ͸߶�
		 */
		querybtn.setBounds(300,80,50,20);
		closebtn.setBounds(500,80,50,20);
		add(querybtn);add(closebtn);
		

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				DbOp.close();
				dispose();
				//System.exit(0);
			}
		});
		querybtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				querybtnActionPerformed(e);
			}
		});
		closebtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
				//System.exit(0);				
			}
		});

		setLocationRelativeTo(null);
		setVisible(true);
	}
	public void querybtnActionPerformed(ActionEvent e){
		bookq=new Object[40][heads.length];
		String sql="",sql1="",sql2="",pubyear="",pubmonth="";
		String[] sqloption={"bookname","author","publisher","publish_time"};
		String[] inputinfor=new String[4];
		boolean impty=false;
		try{
			//��Ӧ����ʱ���ѯ
		if(booktxt[3].getText().equals("")){
			sql2="";
		}else{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			Calendar car=new GregorianCalendar();
			Date pubtime=sdf.parse(booktxt[3].getText());
			car.setTime(pubtime);
			pubyear=String.valueOf(car.get(Calendar.YEAR));
			pubmonth=String.valueOf(car.get(Calendar.MONTH)+1);
			sql2=" year(publish_time)='"+pubyear+"'and month(publish_time)='"+pubmonth+"'";
			//��ʽ��ʱ�䣬ģ����ѯ
		}
		int count=0;
		for(int i=0;i<booktxt.length-1;i++){
			inputinfor[i]=booktxt[i].getText();
			if(inputinfor[i].equals("")){
				//
			}else{
				if(count==0){
					sql1=sqloption[i]+" like'%"+inputinfor[i]+"%' ";
				}else{
					sql1=sql1+" and "+sqloption[i]+" like'%"+inputinfor[i]+"%'";				
				}
				count++;
				impty=true;
			}
		}
		if((!sql1.equals(""))&&(!sql2.equals(""))){
			sql2=" and "+sql2;
		}else{
			
		}
		if((!impty)&&(sql2.equals(""))){
			JOptionPane.showMessageDialog(null,"��ѡ������һ������");
			//scrollpane.setVisible(false);
			return;
		}else{
			sql="select * from book where ";
			sql=sql+sql1+sql2;
			System.out.println(sql);
		}
		boolean rsnext=false;
		//ResultSet rs=DbOp.executeQuery(sql);
		List<Book> listBook=JdbcUtils.getQueryRunner().query(sql, new BeanListHandler<Book>(Book.class));
		
	int i=0;
		for(Book book:listBook){
 				bookq[i][0]=book.getId(); 				bookq[i][1]=book.getBookname();
 				bookq[i][2]=book.getBooktype();			bookq[i][3]=book.getAuthor();
 				bookq[i][4]=book.getTranslator();			bookq[i][5]=book.getPublisher();
 				bookq[i][6]=book.getPublish_time();			bookq[i][7]=book.getPrice();
 				bookq[i][8]=book.getStock();			bookq[i][9]=book.getPage();
			i++;
			rsnext=true;
		}
		if(rsnext){
		}else{
			JOptionPane.showMessageDialog(null,"û�в�ѯ���κ����������Ϣ");
			return;
			//bookq=null;
		}
		table=new JTable(bookq,heads);
		scrollpane=new JScrollPane(table);
		scrollpane.setBounds(50,130,1100,520);
		add(scrollpane);
		}catch(ParseException e9){
			JOptionPane.showMessageDialog(null,"ʱ���ʽ�����磨2012-02��");
		}catch(SQLException e7){
			JOptionPane.showMessageDialog(null,"�Ҳ�����Ҫ��ѯ����Ϣ");
		}
	}
	public static void main(String[] args){
		new BookQuery();
	}
}