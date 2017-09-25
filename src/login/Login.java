package login;
//import PublicModule.DbOp;
import java.awt.Button;
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

import javax.swing.JOptionPane;

import org.apache.commons.dbutils.handlers.BeanHandler;

import entity.Admin;
import util.JdbcUtils;
public class Login extends Frame{
	Toolkit tool= getToolkit();
	String url=Login.class.getResource("/login/login.png").getPath();
	Image img=tool.getImage(url);//getImage():返回一幅图像，该图像从指定文件中获取像素数据，图像格式可以是 GIF、JPEG 或 PNG。
	public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}

	TextField text_user,text_pass;
	public Login(){
		this.setTitle("Please Login");
		this.setLayout(null);
		//setOpaque(true);
		this.setSize(300,150);setResizable(false);
		Label userlb=new Label("UserName:");
		Label passlb=new Label("PassWord:");
		Button sure=new Button("Login");
		Button cancel=new Button("Cancel");
		text_user=new TextField();
		text_pass=new TextField();
		text_pass.setEchoChar('●');
		userlb.setBounds(30,53,70,20);
		passlb.setBounds(30,83,70,20);
		text_user.setBounds(110,50,120,20);
		text_pass.setBounds(110,80,120,20);
		sure.setBounds(42,120,80,25);
		cancel.setBounds(135,120,80,25);
		this.add(text_user);this.add(text_pass);this.add(sure);this.add(cancel);
		sure.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				sureActionListener(e);
				
			}
		});
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();

			}
		});
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
			}
		});
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	public void sureActionListener(ActionEvent le){
		String user=text_user.getText();
		String pass=text_pass.getText();
		String is_admin="";
		if(user.equals("")||pass.equals("")){
			JOptionPane.showMessageDialog(null,"密码不能为空，请输入密码");
			return;
		}
		try{
			String sql="SELECT * FROM USER WHERE username='"+user+"' AND PASSWORD='"+pass+"'";
			//ResultSet rs=DbOp.executeQuery(sql);
			Admin admin=JdbcUtils.getQueryRunner().query(sql, new BeanHandler<Admin>(Admin.class));
				if(admin!=null){
					//is_admin=rs.getString("is_admin");
					is_admin=admin.getIs_admin();
				}else{
					JOptionPane.showMessageDialog(null,"Wrong that is UserNmae or Password ");
					return;
				}
			GlobalVar.login_user=user;				
			ShowMain show=new ShowMain();
			show.setRights(is_admin);
			System.out.println("Successed");
			dispose();
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,"the wrong from information");
		}
	}
	public static void main(String[] args){
		new Login();
	}
}