package login;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.commons.dbutils.handlers.BeanHandler;

import entity.Admin;
import util.JdbcUtils;
public class ChangePassWord extends Frame{
	String[] sign={"�����룺","�趨�����룺","�ظ������룺"};
	Label[] textlb=new Label[3];
	TextField[] passtxt=new TextField[3];
	Button reset=new Button("�������趨");
	public ChangePassWord(){
		setTitle("�޸�����");
		setSize(300,250);
		setLayout(null);
		setResizable(false);
		int y=50;
		for(int i=0;i<3;i++){
			textlb[i]=new Label(sign[i]);
			passtxt[i]=new TextField();
			textlb[i].setBounds(50,y,80,20);
			passtxt[i].setBounds(130,y,100,20);
			passtxt[i].setEchoChar('��');//������������
			add(textlb[i]);add(passtxt[i]);
			y=y+50;
		}
		reset.setBounds(110,200,80,20);
		add(reset);
		setLocationRelativeTo(null);
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setNewPassWord(e);
			}
		});
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
				//System.exit(0);
			}
		});
		setVisible(true);
	}
	public void setNewPassWord(ActionEvent e){
		String[] password=new String[3];
		String sql;
		for (int i=0;i<password.length;i++){
			password[i]=passtxt[i].getText();
		}
		if(!password[0].equals("")){
			sql="select * from user where username='"+GlobalVar.login_user+"'";
		}else{
			JOptionPane.showMessageDialog(null,"Empty String Just Input");
			return;
		}

		try{
			Admin admin=JdbcUtils.getQueryRunner().query(sql, new BeanHandler<Admin>(Admin.class));
			while(admin!=null){
				if(password[0].equals(admin.getPassword())){
					break;
				}else{
					JOptionPane.showMessageDialog(null,"Sorroy It's Fail It's not Old PassWord");
					return;

				}
			}
		}catch(SQLException ee){
			JOptionPane.showMessageDialog(null,"Sorroy It's Fail Some Data Wrong");
			return;
			
		}
		//У�������Ƿ�һ��
		if(password[0].equals(password[1])){
			JOptionPane.showMessageDialog(null,"Sorroy It's Fail Same From Old Password");
			return;
		}
		//У����������ȷ�������Ƿ�һ��
		if(!password[1].equals(password[2])){
			JOptionPane.showMessageDialog(null,"Sorroy It's Fail  Again Password wrong");
			return;
		}
		if(password[1].equals("")&&password[1].equals("")){
			JOptionPane.showMessageDialog(null,"Sorroy It's Fail  one Empty");
			return;
			
		}
		//�޸�����
		sql="update user set password='"+password[1]+"'where username='"+GlobalVar.login_user+"'";
		//sql="UPDATE USER SET PASSWORD='test2' WHERE username='test'";
		int da=0;
		try {
			da = JdbcUtils.getQueryRunner().update(sql);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if(da==1){
			JOptionPane.showMessageDialog(null,"yet It's Successed  Update PassWord");
		}else{
			JOptionPane.showMessageDialog(null,"Sorroy It's Fail  try again");
		}
	} 
	public static void main(String[] args){
		new ChangePassWord();
	}
}