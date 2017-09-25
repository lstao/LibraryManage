package control;

import java.awt.Button;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import login.BorrowReturn;
import util.Utils;
import entity.BorrowerInfo;

public class BorrrowQuery extends JFrame{
	JTable table;
	JScrollPane scrollpane;
	String[] lbsign={"图书编号","图书名字","读者编号","读者姓名"};
	Label[] borrlb=new Label[4];
	TextField[] borrtxt=new TextField[4];
	Button querybtn=new Button("查询");
	Button closebtn=new Button("关闭");
	String[]heads={"图书编号","图书名称","读者","已借天数","是否归还","出版社","出版时间","定价","库存数量","借出日期","还回日期"};
	Object[][] borrkq=new Object[40][heads.length];
	public BorrrowQuery(){
		setTitle("借阅记录查询");
		setLayout(null);
		setSize(1600,900);
		/*
		 * 参数为boolean类型，resizeable值为true时，表示在生成的窗体可以自由改变大小；
		resizeable值为false时，表示生成的窗体大小是由程序员决定的，用户不可以自由.
		 */
		setResizable(false);
		
		int lx=150;
		for(int i=0;i<borrlb.length;i++){
			borrlb[i]=new Label(lbsign[i]);
			borrtxt[i]=new TextField();
			borrlb[i].setBounds(lx,30,50,20);
			borrtxt[i].setBounds(lx+70,30,120,20);
			lx=lx+210;
			add(borrlb[i]);add(borrtxt[i]);
		}
		/*
		 * 前两个分别为x，y轴坐标，后两个分别是长度和高度
		 */
		querybtn.setBounds(300,80,50,20);
		closebtn.setBounds(500,80,50,20);
		add(querybtn);add(closebtn);
		

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
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
		borrkq=new Object[40][heads.length];
		String sql="",sql1="",pubyear="",pubmonth="";
		String[] sqloption={" book.id  "," bookname ", " reader.id  "," readername "};
		String[] inputinfor=new String[4];
		boolean impty=false;
		try{
	//拼接sql语句
				
		
			
		int count=0;
		//从输入框获取输入值
		for(int i=0;i<borrtxt.length;i++){
			inputinfor[i]=borrtxt[i].getText();
			if(inputinfor[i].equals("")){
				//
			}else{
				if("".equals(inputinfor[i])||inputinfor[i]==null) continue;
				if(count==0){
					sql1=sqloption[i]+" like'%"+inputinfor[i]+"%' ";
				}else{
					sql1=sql1+" and "+sqloption[i]+" like'%"+inputinfor[i]+"%'";				
				}
				count++;
				
			}
		}
		System.out.println(sql1);
		if(!"".equals(sql1))
		sql1="and"+sql1;
			sql="select borrow.id id,borrow_date,back_date,if_back, reader.id reader_id,book.id book_id"
				+ ",readertype,readername,max_num,days_num,"+ "bookname,author,publisher,publish_time,price,stock "+" from book,reader,"
						+ "borrow where book.id=borrow.book_id and borrow.reader_id=reader.id  ";
			sql=sql+sql1;
			System.out.println(sql);
		boolean rsnext=false;
		//ResultSet rs=DbOp.executeQuery(sql);
		List<BorrowerInfo> listBorr=BorrowReturn.getBorrwers(sql);		
	String[] informationoption={"id","borrname","borrtype","author","translator","publisher","publish_time","price","stock","page"};
		int i=0;
		String[]heads={"图书编号","图书名称","读者","已借天数","是否归还","出版社","出版时间","定价","库存数量","借出日期","还回日期"};
		for(BorrowerInfo borr:listBorr){
			System.out.println(borr.getReader().getId());
			borrkq[i][0]=borr.getBook().getId();		borrkq[i][1]=borr.getBook().getBookname();
			borrkq[i][2]=borr.getReader().getReadername();	borrkq[i][3]=Utils.getReaderBorrowDays(borr.getBorrower().getBorrow_date(), borr.getBorrower().getBack_date());
			borrkq[i][4]=borr.getBorrower().getIf_back();	borrkq[i][5]=borr.getBook().getPublisher();
			borrkq[i][6]=borr.getBook().getPublish_time();
			borrkq[i][7]=borr.getBook().getPrice();		borrkq[i][8]=borr.getBook().getStock();
			borrkq[i][9]=borr.getBorrower().getBorrow_date();	borrkq[i][10]=borr.getBorrower().getBack_date();
			i++;
			rsnext=true;
		}
			if(rsnext){
			}else{
				JOptionPane.showMessageDialog(null,"没有查询到任何相关联的信息");
				return;
				//borrq=null;
			}
		table=new JTable(borrkq,heads);
		scrollpane=new JScrollPane(table);
		scrollpane.setBounds(50,130,1100,520);
		add(scrollpane);
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	public static void main(String[] args){
		new BorrrowQuery();
	}
}
