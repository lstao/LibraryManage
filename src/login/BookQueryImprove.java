package login;


import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import util.JdbcUtils;
import entity.Book;

public class BookQueryImprove extends Frame implements ActionListener {
	private static final long serialVersionUID = 5477686699015910381L;
	TextField seriesName;
	TextArea bookName;
	Button button;

	BookQueryImprove() { // 构造方法
		super("图书查询");
		setBounds(150, 150, 300, 300);
		seriesName = new TextField(16);
		bookName = new TextArea(5, 10);
		button = new Button("确定");
		Panel p1 = new Panel(), p2 = new Panel();
		p1.add(new Label("请输入丛书名："));
		p1.add(seriesName);
		p2.add(button);
		add(p1, "North");
		add(p2, "South");
		add(bookName, "Center");
		button.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setLocationRelativeTo(null); // 使窗体在屏幕上居中放置
		setVisible(true); // 显示窗体
	}

	public void actionPerformed(ActionEvent e) {
		// 如果当前单击对象为按钮
		if (e.getSource() == button) {
			try {
				bookName.setText(null); // 清空文本区
				ListStudent();
			} catch (SQLException ee) {
			}
		}
	}

	private void ListStudent() throws SQLException {
		String bn1, bn2, sqlcmd;
		// 获取输入的丛书代号
		bn1 = seriesName.getText();
		// 如果丛书代号为空，则直接返回
		if (bn1.length()==0){
			return;
		}
		// 读出全部记录，得到结果集ResultSet对象
		// 创建SQL语句，like表示模糊匹配，%表示任意字符串
		// “丛书代号 like '输入的丛书代号字符串%'”表示查找丛书代号为
		// 以输入的丛书代号字符串开头的全部丛书
		System.out.println(bn1);
		sqlcmd = "select * from book where booktype like '%"+ bn1 + "%'";
		// 执行SQL语句
		List<Book> listBook=JdbcUtils.getQueryRunner().query(sqlcmd, new BeanListHandler<Book>(Book.class));
		boolean boo = false;
		for(Book book:listBook) {
			bn2 = book.getBookname(); // 读取书名
			bookName.append(bn2 + "\n");
			boo = true; // 该系列丛书不为空
		}
		if (boo == false) {
			bookName.append("该系列丛书不存在！");
		}
		//seriesName.setText(bookName.toString());
	}

	public static void main(String[] args) {
		new BookQueryImprove();
	}
}
