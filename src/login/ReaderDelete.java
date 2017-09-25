package login;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import entity.Reader;
import util.JdbcUtils;
public class ReaderDelete extends Frame{
	Toolkit tool= getToolkit();
	String url="bookbk.png";
	Image img=tool.getImage(url);
		public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}

	String[] labelsign={"���߱��","��������","�������","�����Ա�","�ɽ�����","�ɽ�����"};
	Label[] readerlb=new Label[6];
	Label idlb=new Label(labelsign[0]);
	TextField idtxt=new TextField();
	static TextField[] readertxt=new TextField[6];
	Button querybtn,closebtn,updatebtn;
	static Choice readertype,readersex;
	public ReaderDelete(){
		setLayout(null);
		setSize(500,250);setResizable(false);
		setTitle("������Ϣɾ��");
		querybtn=new Button("��ѯ");
		idlb.setBounds(90,40,50,20);
		idtxt.setBounds(150,40,100,20);
		querybtn.setBounds(280,40,50,20);
		add(idlb);add(idtxt);add(querybtn);
		int lx=50,ly=80;
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
				add(readertxt[i]);readertxt[i].setEditable(false);
			}
		readertxt[0].setEditable(false);
		readertype=new Choice();
		readertype.add("��ʦ");
		readertype.add("ѧ��");
		readertype.add("����");
		readertype.add("ְ��");
		readertype.add("����");
		readersex=new Choice();
		readersex.add("��");
		readersex.add("Ů");
		readertxt[2].setVisible(false);
		readertxt[3].setVisible(false);
		readertype.setBounds(110,110,100,20);
		readersex.setBounds(300,110,100,20);
		add(readertype);add(readersex);
		closebtn=new Button("�ر�");
		updatebtn=new Button("ɾ��");
		updatebtn.setBounds(130,190,50,20);
		closebtn.setBounds(310,190,50,20);
		add(updatebtn);add(closebtn);
		querybtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				queryActionPerformed(e);
				
			}
		});
		updatebtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				updateActionPerformed(e);
				clearAllText();
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
	public static void updateActionPerformed(ActionEvent e){
		String id=readertxt[0].getText(),readername=readertxt[1].getText(),readetype=readertype.getSelectedItem().toString();
		String sex=readersex.getSelectedItem().toString();
			if(id.equals("")){
			JOptionPane.showMessageDialog(null,"���߱��Ϊ��,����������߱�ţ���������ѯ��");
			return;
		}
		try{
			int max_num=Integer.parseInt(readertxt[4].getText()),days_num=Integer.parseInt(readertxt[5].getText());
			String sql= "delete  from reader where id='"+id+"'";
			int b = 0;
			try {
				b = JdbcUtils.getQueryRunner().update(sql);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(b==1){
				JOptionPane.showMessageDialog(null,"ɾ��������Ϣ�ɹ�");
			}else{
				JOptionPane.showMessageDialog(null,"������Ϣɾ��ʧ��");
			}
		}catch(NumberFormatException e2){
			JOptionPane.showMessageDialog(null,"�ɽ������Ϳɽ������������");
		}
	}
	public void queryActionPerformed(ActionEvent e){
			String id=idtxt.getText();
			if(id.equals("")){
				JOptionPane.showMessageDialog(null,"���߱�Ų�Ϊ��");
				return;
			}
			if(JdbcUtils.IfReaderExit(id)){
				Reader reader=ReaderSelect.SelectReaderByID(id);
				String[] newreader=reader.getReaderInformation();
				if(reader!=null){
					readersex.select(reader.getSex());				
					readertype.select(reader.getReadertype());
					for(int i=0;i<readertxt.length;i++){
						//������ڶ����У��ṩ���ض�Ӧ��string[]�����㸴�ƵĴ���
						readertxt[i].setText(newreader[i]);
					}
				}
			}else{
				JOptionPane.showMessageDialog(null,"�����ڸö���");	
			}
	}
	public void clearAllText(){
		for(int i=0;i<readerlb.length;i++){
			readerlb[i].setText("");
		}
	}
	public static void main(String[] args){
		new ReaderDelete();
	}
}