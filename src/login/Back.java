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
	Label bookidlb = new Label("图书编号"), readeridlb = new Label("读者编号");
	TextField bookidtxt = new TextField(), readeridtxt = new TextField();
	Button querybtn = new Button("查询"), borrowbtn = new Button("还书"),
			closebtn = new Button("清除");
	String SepLine = "--------------------------------------------------";
	String[] sep = { "图书信息", "读者信息", "借阅信息" };
	Label[] seplabel = new Label[3];
	String[] optionname = { "书名：", "作者：", "出版社：", "出版时间：", "定价：", "存量：", "姓名：","类型：", "可借数：", "可借天数：", "借阅日期：","阅读天数：","还书日期" };
	Label[] alloption = new Label[13];
	Label[] showoption = new Label[13];

	public Back() {
		setTitle("图书归还");
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
				System.out.println(i);// /太奇怪了
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
			String sql = "update borrow set back_date='"+borrowbackdate+"',if_back='是'";
			sql=sql+"where book_id='"+bookid+"'and reader_id='"+readerid+"'and if_back='否'";
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
				JOptionPane.showMessageDialog(null, "还书成功");

			} else {
				JOptionPane.showMessageDialog(null, "还书数据登记失败！");
			}
			setInitialize();
		}
	}

	public void queryActionPerformed(ActionEvent e) {
		String bookid = bookidtxt.getText(), readerid = readeridtxt.getText();
		if (!bookid.equals("") && !readerid.equals("")) {
		} else {
			JOptionPane.showMessageDialog(null, "图书编号和读者编号都不可以为空");
			setInitialize();
			return;
		}
		Book book = BookSelect.SelectBookByID(bookid);
		Reader reader = ReaderSelect.SelectReaderByID(readerid);
		Borrower borr=null;
		if ((borr=IfBorrowBack.findBook(bookid, readerid))==null) {
			JOptionPane.showMessageDialog(null,"查询不到该读者借阅这本书");
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
			JOptionPane.showMessageDialog(null, "不存在该图书或该读者");
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