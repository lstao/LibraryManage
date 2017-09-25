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

	BookQueryImprove() { // ���췽��
		super("ͼ���ѯ");
		setBounds(150, 150, 300, 300);
		seriesName = new TextField(16);
		bookName = new TextArea(5, 10);
		button = new Button("ȷ��");
		Panel p1 = new Panel(), p2 = new Panel();
		p1.add(new Label("�������������"));
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
		setLocationRelativeTo(null); // ʹ��������Ļ�Ͼ��з���
		setVisible(true); // ��ʾ����
	}

	public void actionPerformed(ActionEvent e) {
		// �����ǰ��������Ϊ��ť
		if (e.getSource() == button) {
			try {
				bookName.setText(null); // ����ı���
				ListStudent();
			} catch (SQLException ee) {
			}
		}
	}

	private void ListStudent() throws SQLException {
		String bn1, bn2, sqlcmd;
		// ��ȡ����Ĵ������
		bn1 = seriesName.getText();
		// ����������Ϊ�գ���ֱ�ӷ���
		if (bn1.length()==0){
			return;
		}
		// ����ȫ����¼���õ������ResultSet����
		// ����SQL��䣬like��ʾģ��ƥ�䣬%��ʾ�����ַ���
		// ��������� like '����Ĵ�������ַ���%'����ʾ���Ҵ������Ϊ
		// ������Ĵ�������ַ�����ͷ��ȫ������
		System.out.println(bn1);
		sqlcmd = "select * from book where booktype like '%"+ bn1 + "%'";
		// ִ��SQL���
		List<Book> listBook=JdbcUtils.getQueryRunner().query(sqlcmd, new BeanListHandler<Book>(Book.class));
		boolean boo = false;
		for(Book book:listBook) {
			bn2 = book.getBookname(); // ��ȡ����
			bookName.append(bn2 + "\n");
			boo = true; // ��ϵ�д��鲻Ϊ��
		}
		if (boo == false) {
			bookName.append("��ϵ�д��鲻���ڣ�");
		}
		//seriesName.setText(bookName.toString());
	}

	public static void main(String[] args) {
		new BookQueryImprove();
	}
}
