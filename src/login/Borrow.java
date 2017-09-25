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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.dbutils.handlers.BeanListHandler;

import util.JdbcUtils;
import util.Utils;
import entity.Book;
import entity.Borrower;
import entity.Reader;

public class Borrow extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7670129939284773294L;
	Label bookidlb = new Label("图书编号"), readeridlb = new Label("读者编号");
	TextField bookidtxt = new TextField(), readeridtxt = new TextField();
	Button querybtn = new Button("查询"), borrowbtn = new Button("借出"),
			closebtn = new Button("清除");
	String SepLine = "--------------------------------------------------";
	String[] sep = { "图书信息", "读者信息", "借阅信息" };
	Label[] seplabel = new Label[3];
	String[] optionname = { "书名：", "作者：", "出版社：", "出版时间：", "定价：", "存量：", "姓名：",
			"类型：", "可借数：", "可借天数：", "已借数量：", "是否可借：", "借阅日期：" };
	Label[] alloption = new Label[13];
	Label[] showoption = new Label[13];

	public Borrow() {
		setTitle("图书借出");
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

	@SuppressWarnings("static-access")
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
		Borrower borrow;
		if ((borrow=IfBorrowBack.findBook(bookid, readerid))!=null) {
			Date date_1 = new Date();

			Date date2 = borrow.getBorrow_date();
			String fulldays = Utils.getReaderBorrowDays(date2, date_1);
			System.out.println("fulldays:  " + fulldays);
			JOptionPane.showMessageDialog(null, book.getBookname() + "已被"
					+ reader.getReadername() + "借阅" + fulldays
					+ "天，且还未归还，该读者不能借阅此图书");
			setInitialize();
			return;

		} 
		if (book != null && reader != null) {
			if (reader.getMax_num() <=getReaderBorrowBookCounts(readerid)) {
				JOptionPane.showMessageDialog(null, "对不起该读者借阅图书太多，不能再借阅");
				return;
			}

			if (Integer.parseInt(getImposeRestrictionsOnDays_num(readerid)) > reader
					.getDays_num()) {
				JOptionPane.showMessageDialog(null, "对不起"
						+ reader.getReadername() + "借阅天数已超过"
						+ reader.getDays_num() + "天，不能再借阅");
				return;
			}
			if (book.getStock() <= 0) {
				JOptionPane.showMessageDialog(null, "对不起该图书已无库存，不能再借阅");
				return;
			}
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

		showoption[10].setText(String.valueOf(getReaderBorrowBookCounts(readerid)));
		showoption[12].setText(sdf.format(currentdate));
		showoption[11].setText("是");
		borrowbtn.setEnabled(true);
		bookidtxt.setEditable(false);
		readeridtxt.setEditable(false);
	}

	public void borrowActionPerformed(ActionEvent e) {
		String bookid = bookidtxt.getText(), readerid = readeridtxt.getText();
		if (!bookid.equals("") && !readerid.equals("")) {
			Date currentdate = new Date();
			String borrowdate = showoption[12].getText();
			String IOL = getInsertOrderedList();
			String sql = "insert into borrow(id,book_id,reader_id,borrow_date,if_back)  values('"
					+ IOL
					+ "','"
					+ bookid
					+ "','"
					+ readerid
					+ "','"
					+ borrowdate + "','否')";
			String sql1 = "update book set stock='"
					+ (Integer.parseInt(showoption[5].getText()) - 1) + "'"
					+ "where id='" + bookid + "'";
			int success = 0;
			try {
				success = JdbcUtils.getQueryRunner().update(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (success == 1) {
				DbOp.executeUpdate(sql1);
				JOptionPane.showMessageDialog(null, "借书成功");

			} else {
				JOptionPane.showMessageDialog(null, "借书数据登记失败！");
			}
			setInitialize();
		}
	}
	// 读者借阅最早图书未归还天数；
	public String getImposeRestrictionsOnDays_num(String readerid) {
		String sql, mydate = "";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sql = "select * from borrow where reader_id='" + readerid
				+ "' and if_back='否'";
		List<Borrower> listBorrower = null;
		try {
			listBorrower = JdbcUtils.getQueryRunner().query(sql, new BeanListHandler<Borrower>(Borrower.class));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int a = 0;
		if(listBorrower==null){
			return "0";
		}
		int[] bookborrowdays = new int[listBorrower.size()];
		for(Borrower b:listBorrower){
			bookborrowdays[a] = Integer.parseInt(Utils.getReaderBorrowDays(
					 b.getBorrow_date(), date));
			a++;
		}
		int temp = -1;
		for (int i = 0; i < bookborrowdays.length; i++) {
			if (bookborrowdays[i] > temp) {
				temp = bookborrowdays[i];
			}
		}
		mydate = String.valueOf(temp);
		return mydate;
	}


	// 获取读者借阅未还数量
	public int getReaderBorrowBookCounts(String id) {
		List<Borrower> listBorrower=null;
		try {
			String sql = "select * from borrow where reader_id='" + id + "' and if_back='否'";
			 listBorrower=JdbcUtils.getQueryRunner().query(sql, new BeanListHandler<Borrower>(Borrower.class));
			//优化，查询汇总？？？
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "借书数量查询失败");
		}
		if(listBorrower!=null)
		return listBorrower.size();
		return 0;
	}

	// 自动生成借阅表编号
	public String getInsertOrderedList() {
		String returnstring = "";
		String sql = "select * from borrow";
		List<Borrower> listBorrower =null;
		try {
			 listBorrower = JdbcUtils.getQueryRunner().query(sql, new BeanListHandler<Borrower>(Borrower.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(listBorrower==null){
				return "000000000";
			}
			int count =listBorrower.size();
			String[] allid = new String[count];
			int[] intofid = new int[count];

			int i = 0;int temp = -1;
			for(Borrower b:listBorrower){
				allid[i] = b.getId();
				intofid[i] = Integer.parseInt(allid[i]);
				if (intofid[i] > temp) {
					temp = intofid[i];
				}
				i++;
			}
			returnstring = String.valueOf(temp + 1);
			int len = returnstring.length();
			for (int f = 0; f < 9 - len; f++) {
				returnstring = "0" + returnstring;

			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}

		return returnstring;
	}
	//初始化
	public void setInitialize() {
		for (int i = 0; i < showoption.length; i++) {
			showoption[i].setText("xxxxx");

		}
		bookidtxt.setText("");
		readeridtxt.setText("");
		bookidtxt.setEditable(true);
		readeridtxt.setEditable(true);
		//设置借出表单不可按
		borrowbtn.setEnabled(false);
	}

	public static void main(String[] args) {
		new Borrow();
	}
}
