package login;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import entity.Book;
import util.JdbcUtils;
public class BookAdd extends Frame implements ActionListener{
	Toolkit tool= getToolkit();
	String url=Login.class.getResource("/login/bookbk.png").getPath();
	Image img=tool.getImage(url);
	String[] lbname={"图书编号","图书名称","图书页数","图书作者","翻     译","出 版 社","出版时间","定价","库存数量","所属类型"};
	Label[] booklb=new Label[10];
	TextField[] booktxt=new TextField[10];
	Button savebtn=new Button("Save");
	Button closebtn=new Button("Close");
	Choice booktype=new Choice();
	
	public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}

	public void clearAndSetBookId(){
		for(int j=0;j<booktxt.length;j++){
			booktxt[j].setText("");
		}
		String str=getInsertOrderedList();
		booktxt[0].setEditable(false);
		booktxt[0].setText(str);

	}

	
	public BookAdd(){
		setTitle("添加新书");
		setLayout(null);
		setSize(500,250);
		setResizable(false);
		//this.setOpaque(false);
		this.setForeground(Color.BLACK);
		int lx=50,ly=50;
		booktype.add("程序设计");
		booktype.add("图形设计");
		booktype.add("其他");
		booktype.add("科技");
		booktype.add("文学");
		booktype.add("历史");
		booktype.add("百科");
		booktype.add("英语");
		booktype.add("计算机");
		booktype.add("Internet");
		booktype.add("数学");
		String str=getInsertOrderedList();
		for(int i=0;i<booklb.length;i++){
			if(lx>240){
				lx=50;
				ly=ly+30;
			}
			booklb[i]=new Label(lbname[i]);
			booklb[i].setBounds(lx,ly,50,20);
			booktxt[i]=new TextField();
			booktxt[i].setBounds(lx+60,ly,100,20);
			lx=lx+190;
			add(booklb[i]);add(booktxt[i]);
			}
		//设置图书类型以及所属种类不可编辑？
		booktxt[0].setEditable(false);
		booktxt[0].setText(str);

		booktxt[9].setVisible(false);
		booktype.setBounds(300,170,100,20);
		add(booktype);
		//保存以及关闭按钮
		savebtn.setBounds(150,210,80,25);
		closebtn.setBounds(280,210,80,25);
		add(savebtn);add(closebtn);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				DbOp.close();
				dispose();	
			}
		});
		savebtn.addActionListener(this);
		closebtn.addActionListener(this);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static String getInsertOrderedList(){
		String returnstring="";
		String sql="select id from book";
		
		try{
		
		List<Book> listBook=JdbcUtils.getQueryRunner().query(sql, new BeanListHandler<Book>(Book.class));
		int count=listBook.size();
		String[] allid=new String[count];
		int[] intofid=new int[count];
		int i=0;int max=0;int temp=-1;
		for(Book book:listBook){
			//因为id的存储是A000001开始，所有裁剪一下，这个循环提取所有的序号，强转为数字
			allid[i]=book.getId();
			String mystr=allid[i].substring(1);
			intofid[i]=Integer.parseInt(mystr);
			if(intofid[i]>temp){
				temp=intofid[i];	
			}
			i++;
		}
		/*
		for(int j=0;j<intofid.length;j++){
			if(intofid[j]>temp){
				temp=intofid[j];		
			}
		}
		*/
		returnstring=String.valueOf(temp+1);
		int len=returnstring.length();
		for(int f=0;f<5-len;f++){
			returnstring="0"+returnstring;
			
		}
		returnstring="A"+returnstring;
		}catch(SQLException ee){

		}
		
		return returnstring;
	}

	public  void actionPerformed(ActionEvent e){
		Object ob=e.getSource();
		if(ob==savebtn){
			savebtnActionPerformed();
			clearAndSetBookId();
		}
		if(ob==closebtn){
				DbOp.close();
				dispose();				
			
		}
	}
	public  void savebtnActionPerformed(){
		String id=booktxt[0].getText();
		String typestr=booktype.getSelectedItem().toString();
		String[] inputstring=new String[9];
		boolean emptyequals=false;
		for(int i=0;i<inputstring.length;i++){
			inputstring[i]=booktxt[i].getText();
			if(inputstring[i].equals("")){
				JOptionPane.showMessageDialog(null,"请务必填写完整");
				return;
			}
		}
		if(id.equals("")){
			JOptionPane.showMessageDialog(null,"图书编号不能为空");
			return;
		}
		if(JdbcUtils.IfBookIdExit(id)){
			JOptionPane.showMessageDialog(null,"图书编号已存在");
			return;
		}
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			sdf.parse(inputstring[6]);
			float price=Float.parseFloat(inputstring[7]);
			int stock= Integer.parseInt(inputstring[8]);
			int page=Integer.parseInt(inputstring[2]);
			String sql="insert into book(id,bookname,booktype,author,translator,publisher,publish_time,price,stock,page)";
			sql=sql+"values('"+id+"','"+inputstring[1]+"','"+typestr+"','"+inputstring[3]+"','"+inputstring[4]+"','";
			sql=sql+inputstring[5]+"','"+inputstring[6]+"',"+price+","+stock+","+page+")";
			int i=0;
			try {
				i = JdbcUtils.getQueryRunner().update(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(i==1){
				JOptionPane.showMessageDialog(null,"图书添加成功！");
				clearAllText();
			}
		}catch(ParseException e2){
			JOptionPane.showMessageDialog(null,"出版时间格式错误，正确为（年-月）");
		}catch(NumberFormatException e1){
			JOptionPane.showMessageDialog(null,"库存数量，价格，页数错误，应为数字");
		}
	}
	
	public void clearAllText(){
		for(int i=0;i<booktxt.length;i++){
			booktxt[i].setText("");
		}
	}
	public static void main(String[] args){
		new BookAdd();
	}
}