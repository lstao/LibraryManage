package login;
import java.awt.Button;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import entity.Reader;
import util.JdbcUtils;
public class ReaderQuery extends JFrame{
	Label namelb=new Label("读者姓名"),typelb=new Label("读者类型");
	TextField nametxt=new TextField(),typetxt=new TextField();
	Button querybtn=new Button("查询"),closebtn=new Button("退出");
	JTable table;
	JScrollPane scrollpane;
	String[] heads={"读者编号","读者姓名","读者类型","读者性别","可借数量","可借天数"};
	Object[][] readertable=new Object[30][heads.length];
	public ReaderQuery(){
		setTitle("读者查询");
		setSize(700,400);
		setLayout(null);
		setResizable(false);
		
		namelb.setBounds(150,30,50,20);
		nametxt.setBounds(210,30,100,20);
		typelb.setBounds(330,30,50,20);
		typetxt.setBounds(390,30,100,20);
		querybtn.setBounds(220,80,50,20);
		closebtn.setBounds(410,80,50,20);
		add(namelb);add(nametxt);add(typelb);add(typetxt);add(querybtn);add(closebtn);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
			}
		});
		querybtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				 queryActionPerformed(e);
			}
		});
		closebtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});

		setLocationRelativeTo(null);
		setVisible(true);
	}
	private void queryActionPerformed(ActionEvent e){
		readertable=new Object[30][heads.length];
		String readername=nametxt.getText(),readertype=typetxt.getText();
		String sql,sql1,sql2,sql3;
		if(readername.equals("")){
			sql1="";
		}else{
			sql1=" readername like '%"+readername+"%'";
		}
		if(readertype.equals("")){
			sql2="";
		}else{
			sql2="readertype like '%"+readertype+"%'";
		}
		if(!readername.equals("")&&!readertype.equals("")){
			sql2="and "+sql2;
		}
		sql="select * from reader where ";
		sql3=sql1+sql2;
		if(!sql3.equals("")){
			sql=sql+sql3;
		}else{
			JOptionPane.showMessageDialog(null,"请输入你要查询的信息");
			return;
		}
		try{
			List<Reader> listReader=JdbcUtils.getQueryRunner().query(sql, new BeanListHandler<Reader>(Reader.class));
			String[] alloptions={"id","readername","readertype","sex","max_num","days_num"};
			int i=0;
			boolean rsnext=false;
			for(Reader reader:listReader){
				readertable[i][0]=reader.getId();	readertable[i][1]=reader.getReadername();
				readertable[i][2]=reader.getReadertype();	readertable[i][3]=reader.getSex();
				readertable[i][4]=reader.getMax_num();		readertable[i][5]=reader.getDays_num();
				i++;
				rsnext=true;
			}
			if(rsnext){
				table=new JTable(readertable,heads);
				scrollpane=new JScrollPane(table);
				scrollpane.setBounds(50,120,600,240);
				add(scrollpane);

				
			}else{
				JOptionPane.showMessageDialog(null,"没有查询到任何相关联的信息");
			}
		}catch(SQLException se){
			JOptionPane.showMessageDialog(null,"找不到你要查询的信息");

		}
		
	}
	public static void main(String[] args){
		new ReaderQuery();
	}
}