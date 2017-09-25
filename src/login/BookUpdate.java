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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import entity.Book;
import util.JdbcUtils;
public class BookUpdate extends Frame{
	Toolkit tool= getToolkit();
	String url="bookbk.png";
	Image img=tool.getImage(url);
		public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}

	String[] lbname={"ͼ����","ͼ������","ͼ��ҳ��","ͼ������","��     ��","�� �� ��","����ʱ��","����","�������","��������"};
	//��Ӧ��ͼ����ϸ��Ŀ������д
	Label[] booklb=new Label[10];
	//���ں�����Ӧ�ı�
	TextField[] booktxt=new TextField[10];
	Label idlb=new Label("ͼ����");
	TextField idtxt=new TextField();
	Button savebtn=new Button("Save");
	Button closebtn=new Button("Close");
	Button querybtn=new Button("��ѯ");
	Button clearbtn=new Button("���");
	Choice booktype=new Choice();
	public BookUpdate(){
		setTitle("ͼ����Ϣ�޸�");
		setLayout(null);
		setSize(500,280);setResizable(false);
		int lx=50,ly=80;
		booktype.add("�������");
		booktype.add("ͼ�����");
		booktype.add("����");
		booktype.add("�Ƽ�");
		booktype.add("��ѧ");
		booktype.add("��ʷ");
		booktype.add("�ٿ�");
		booktype.add("Ӣ��");
		booktype.add("�����");
		booktype.add("Internet");
		booktype.add("��ѧ");

		idlb.setBounds(100,40,50,20);
		idtxt.setBounds(160,40,100,20);
		querybtn.setBounds(280,40,80,20);
		add(idlb);add(idtxt);add(querybtn);
		for(int i=0;i<booklb.length;i++){
			if(lx>240){
				lx=50;
				ly=ly+30;
			}
			booklb[i]=new Label(lbname[i]);
			booktxt[i]=new TextField();
			booklb[i].setBounds(lx,ly,50,20);
			booktxt[i].setBounds(lx+60,ly,100,20);
			lx=lx+190;
			add(booklb[i]);add(booktxt[i]);
		}
		//booktxt[0].setEditable(false);
		
		booktxt[9].setVisible(false);
		booktype.setBounds(300,200,100,20);
		add(booktype);
		savebtn.setBounds(150,240,80,25);
		closebtn.setBounds(280,240,80,25);
		clearbtn.setBounds(420,50,80,25);
		add(savebtn);add(closebtn);
		//������ա�=
		add(clearbtn);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				//�˳���ǰ���ڣ���������������
				dispose();	
			}
		});
		closebtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();			
			}
		});
		//ִ�в�ѯ�ļ�����
		querybtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			querybtnActionPerformed();
						
			}
		});
		//ִ�б���ļ�����
		savebtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			savebtnActionPerformed();
						
			}
		});
		
		clearbtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				clearAllText();
			}
		
		});
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public  void querybtnActionPerformed(){
		String id=idtxt.getText();
		if(id.equals("")){
			JOptionPane.showMessageDialog(null,"ͼ���Ų���Ϊ��");
			return;
		}
		if(JdbcUtils.IfBookIdExit(id)){
			//��ȡͼ����Ϣ��������ͼ�����ͣ������Դ���chose�Դ�apiע�����ݣ���������setTextע������
			Book book =BookSelect.SelectBookByID(id);
			String[] infor=book.getBookinformation();
			if(book!=null){
				booktype.select(book.getBooktype());
				for(int i=0;i<infor.length;i++){
					booktxt[i].setText(infor[i]);
				}
			}		
		}else{	
			JOptionPane.showMessageDialog(null,"û�и�ͼ��");
		}
	}
	public  void savebtnActionPerformed(){
		String id=booktxt[0].getText();
		//ѡ����ʽ������
		String typestr=booktype.getSelectedItem().toString();
		String[] inputstring=new String[9];
		for(int i=0;i<inputstring.length;i++){
			inputstring[i]=booktxt[i].getText();
		}
		if(id.equals("")){
			JOptionPane.showMessageDialog(null,"ͼ���Ų���Ϊ��");
			return;
		}
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			sdf.parse(inputstring[6]);
			float price=Float.parseFloat(inputstring[7]);
			int stock= Integer.parseInt(inputstring[8]);
			int page=Integer.parseInt(inputstring[2]);
			String sql="update book set bookname='"+inputstring[1]+"',booktype='"+typestr+"',author='"+inputstring[3];
			sql=sql+"',translator='"+inputstring[4]+"',publisher='"+inputstring[5]+"',publish_time='"+inputstring[6];
			sql=sql+"',price='"+price+"',stock='"+stock+"',page='"+page+"' where id='"+id+"'";

			int i = 0;
			try {
				i = JdbcUtils.getQueryRunner().update(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(i==1){
				JOptionPane.showMessageDialog(null,"ͼ���޸ĳɹ���");
				clearAllText();
			}
		}catch(ParseException e2){
			JOptionPane.showMessageDialog(null,"����ʱ���ʽ������ȷΪ����-�£�");
		}catch(NumberFormatException e1){
			JOptionPane.showMessageDialog(null,"����������۸�ҳ������ӦΪ����");
		}
	}
	//Ϊʲô����������ݣ�������
	public void clearAllText(){
		for(int i=0;i<9;i++){
			booktxt[i].setText("");
		}
	}

	public static void main(String[] args){
		new BookUpdate();
	}
}