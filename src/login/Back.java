package login;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import util.JdbcUtils;
import util.Utils;
import entity.Book;
import entity.Borrower;
import entity.Reader;

public class Back extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7670129939284773294L;
	Label bookidlb = new Label("ͼ����"), readeridlb = new Label("���߱��");
	TextField bookidtxt = new TextField(), readeridtxt = new TextField();
	Button querybtn = new Button("��ѯ"), borrowbtn = new Button("����"),
			closebtn = new Button("���");
	String SepLine = "--------------------------------------------------";
	String[] sep = { "ͼ����Ϣ", "������Ϣ", "������Ϣ" };
	Label[] seplabel = new Label[3];
	String[] optionname = { "������", "���ߣ�", "�����磺", "����ʱ�䣺", "���ۣ�", "������", "������","���ͣ�", "�ɽ�����", "�ɽ�������", "�������ڣ�","�Ķ�������","��������" };
	Label[] alloption = new Label[13];
	Label[] showoption = new Label[13];

	public Back() {
		setTitle("ͼ��黹");
		setLayout(null);
		setSize(500, 470);
		setResizable(false);
		this.setForeground(Color.BLACK);
		bookidlb.setBounds(50, 50, 50, 20);
		bookidtxt.setBounds(110, 50, 100, 20);
		readeridlb.setBounds(220, 50, 50, 20);
		readeridtxt.setBounds(280, 50, 100, 20);
		querybtn.setBounds(400, 50, 50, 20);
		add(bookidlb);
		add(bookidtxt);
		add(readeridlb);
		add(readeridtxt);
		add(querybtn);
		int lx = 50, ly = 90, i = 0, k = 0;
		for (int j = 0; j < alloption.length; j++) {
			if (lx > 300) {
				lx = 50;
				ly = ly + 30;
			}
			if (ly == 90 || ly == 210 || ly == 300) {
				System.out.println(i);// /̫�����
				seplabel[i] = new Label(SepLine + sep[i] + SepLine);
				seplabel[i].setBounds(20, ly, 440, 20);
				add(seplabel[i]);
				j--;
				k++;
				if (k <= 1) {
					i = 0;
				}
				if (k == 2 || k == 3) {
					i = 1;
				}
				if (k == 4) {
					i = 2;
				}
			} else {
				alloption[j] = new Label(optionname[j]);
				showoption[j] = new Label("");
				alloption[j].setBounds(lx, ly, 70, 20);
				showoption[j].setBounds(lx + 70, ly, 150, 20);
				add(alloption[j]);
				add(showoption[j]);
			}
			lx = lx + 250;
		}
		borrowbtn.setBounds(110, 400, 50, 20);
		closebtn.setBounds(310, 400, 50, 20);
		add(borrowbtn);
		add(closebtn);
		borrowbtn.setEnabled(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				//System.exit(0);
			}
		});
		querybtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queryActionPerformed(e);
			}
		});

		borrowbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				borrowActionPerformed(e);
			}
		});
		closebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setInitialize();	
			}
		});

		setLocationRelativeTo(null);
		setVisible(true);
	}
	public void borrowActionPerformed(ActionEvent e) {
		String bookid = bookidtxt.getText(), readerid = readeridtxt.getText();
		if (!bookid.equals("") && !readerid.equals("")) {
			Date currentdate = new Date();
			String borrowbackdate = showoption[12].getText();
			String sql = "update borrow set back_date='"+borrowbackdate+"',if_back='��'";
			sql=sql+"where book_id='"+bookid+"'and reader_id='"+readerid+"'and if_back='��'";
			String sql1 = "update book set stock='"
					+ (Integer.parseInt(showoption[5].getText()) + 1) + "'"
					+ "where id='" + bookid + "'";
			int success = 0;
			try {
				success = JdbcUtils.getQueryRunner().update(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (success == 1) {
				DbOp.executeUpdate(sql1);
				JOptionPane.showMessageDialog(null, "����ɹ�");

			} else {
				JOptionPane.showMessageDialog(null, "�������ݵǼ�ʧ�ܣ�");
			}
			setInitialize();
		}
	}

	public void queryActionPerformed(ActionEvent e) {
		String bookid = bookidtxt.getText(), readerid = readeridtxt.getText();
		if (!bookid.equals("") && !readerid.equals("")) {
		} else {
			JOptionPane.showMessageDialog(null, "ͼ���źͶ��߱�Ŷ�������Ϊ��");
			setInitialize();
			return;
		}
		Book book = BookSelect.SelectBookByID(bookid);
		Reader reader = ReaderSelect.SelectReaderByID(readerid);
		Borrower borr=null;
		if ((borr=IfBorrowBack.findBook(bookid, readerid))==null) {
			JOptionPane.showMessageDialog(null,"��ѯ�����ö��߽����Ȿ��");
			setInitialize();
			return;

		} else {

		}
		if (book != null && reader != null) {
			showoption[0].setText(book.getBookname().toString());
			showoption[1].setText(book.getAuthor().toString());
			showoption[2].setText(book.getPublisher().toString());
			showoption[3].setText(book.getPublish_time().toString());
			showoption[4].setText(String.valueOf(book.getPrice()));
			showoption[5].setText(String.valueOf(book.getStock()));
			showoption[6].setText(reader.getReadername());
			showoption[7].setText(reader.getReadertype());
			showoption[8].setText(String.valueOf(reader.getMax_num()));
			showoption[9].setText(String.valueOf(reader.getDays_num()));
		} else {
			JOptionPane.showMessageDialog(null, "�����ڸ�ͼ���ö���");
			setInitialize();
			return;

		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date currentdate = new Date();
		String daycutearlyday=Utils.getReaderBorrowDays(borr.getBorrow_date(),currentdate);
		showoption[10].setText(sdf.format(borr.getBorrow_date()));
		showoption[12].setText(sdf.format(currentdate));
		showoption[11].setText(daycutearlyday);
		borrowbtn.setEnabled(true);
		bookidtxt.setEditable(false);
		readeridtxt.setEditable(false);
	}


	public void setInitialize() {
		for (int i = 0; i < showoption.length; i++) {
			showoption[i].setText("xxxxx");

		}
		bookidtxt.setText("");
		readeridtxt.setText("");
		bookidtxt.setEditable(true);
		readeridtxt.setEditable(true);
		borrowbtn.setEnabled(false);
	}

	public static void main(String[] args){
		new Back();
	}
}