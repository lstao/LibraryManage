package login;
import java.awt.Button;
import java.awt.Choice;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import util.JdbcUtils;
import entity.Reader;
public class ReaderAdd extends Frame{
	Toolkit tool= getToolkit();
	String url="bookbk.png";
	Image img=tool.getImage(url);
		public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}

	public void clearAndSetReaderId(){
		for(int j=0;j<readertxt.length;j++){
			readertxt[j].setText("");
		}
		String str;
		str=getInsertOrderedList();
		readertxt[0].setText(str);

	}
	String[] labelsign={"读者编号","读者姓名","读者类别","读者性别","可借数量","可借天数"};
	Label[] readerlb=new Label[6];
	static TextField[] readertxt=new TextField[6];
	Button querybtn,closebtn;
	static Choice readertype,readersex;
	public ReaderAdd(){
		setLayout(null);
		setSize(500,200);setResizable(false);
		setTitle("添加新读者");
		String str=getInsertOrderedList();
		int lx=50,ly=50;
			for(int i=0;i<readertxt.length;i++){
				if(lx>240){
					ly=ly+30;
					lx=50;
				}
				readerlb[i]=new Label(labelsign[i]);
				readertxt[i]=new TextField();
				readerlb[i].setBounds(lx,ly,50,20);
				readertxt[i].setBounds(lx+60,ly,100,20);
				lx=lx+190;
				add(readerlb[i]);
				add(readertxt[i]);
			}
		str=getInsertOrderedList();
		readertxt[0].setEditable(false);
		readertxt[0].setText(str);
		readertype=new Choice();
		readertype.add("教师");
		readertype.add("学生");
		readertype.add("作家");
		readertype.add("职工");
		readertype.add("其他");
		readersex=new Choice();
		readersex.add("男");
		readersex.add("女");
		readertxt[2].setVisible(false);
		readertxt[3].setVisible(false);
		readertype.setBounds(110,80,100,20);
		readersex.setBounds(300,80,100,20);
		add(readertype);add(readersex);
		querybtn=new Button("Add");
		closebtn=new Button("Close");
		querybtn.setBounds(130,140,50,20);
		closebtn.setBounds(310,140,50,20);
		add(querybtn);add(closebtn);
		querybtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				updateActionPerformed(e);
				clearAndSetReaderId();
				
			}
		});
		closebtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
				//System.exit(0);
			}
		});
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
				//System.exit(0);
			}
		});
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static String getInsertOrderedList(){
		String returnstring="";
		String sql="select * from reader";
		
		try{
		
		List<Reader> listReader=JdbcUtils.getQueryRunner().query(sql, new BeanListHandler<Reader>(Reader.class));
		int count=listReader.size();
		String[] allid=new String[count];
		int[] intofid=new int[count];
	
		int i=0;int temp=-1;
		for(Reader r:listReader){
			allid[i]=r.getId();
			intofid[i]=Integer.parseInt(allid[i]);
			if(intofid[i]>temp){
				temp=intofid[i];
			}
			i++;
		}
		
		returnstring=String.valueOf(temp+1);
		int len=returnstring.length();
		for(int f=0;f<5-len;f++){
			returnstring="0"+returnstring;
			
		}
		}catch(SQLException ee){

		}
		
		
		return returnstring;
	}
	public static void updateActionPerformed(ActionEvent e){
		String[] readerstr=new String[6];
		readerstr[2]=readertype.getSelectedItem().toString();
		readerstr[3]=readersex.getSelectedItem().toString();
		for(int i=0;i<readerstr.length;i++){
			if(i==2||i==3){
				continue;
			}
			readerstr[i]=readertxt[i].getText();
			if(readerstr[i].equals("")){
				JOptionPane.showMessageDialog(null,"请务必填写完整");
				return;
			}
		}
		String id=readerstr[0];
		if(JdbcUtils.IfReaderExit(id)){
			JOptionPane.showMessageDialog(null,"该读者已经存在！");
			return;
		}
		try{
			int max_num=Integer.parseInt(readerstr[4]);
			int days_num=Integer.parseInt(readerstr[5]);
			String sql="insert into reader(id,readername,readertype,sex,max_num,days_num) values('";
			sql=sql+id+"','"+readerstr[1]+"','"+readerstr[2]+"','"+readerstr[3]+"',"+max_num+","+days_num+")";
			int a = 0;
			try {
				a = JdbcUtils.getQueryRunner().update(sql);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(a==1){
				JOptionPane.showMessageDialog(null,"读者添加成功");
			}else{
				JOptionPane.showMessageDialog(null,"读者添加失败");
			}
		}catch(NumberFormatException e1){
			JOptionPane.showMessageDialog(null,"可借数量和可借天数必须是整数");
		}
	}
	public static void main(String[] args){
		new ReaderAdd();
	}
}