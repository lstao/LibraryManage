package util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
	
	public static String getReaderBorrowDays(Date date1, Date date2) {// 计算时间差
		if(date1==null||date2==null){
			return getReaderBorrowDays(date1,new Date());
		}
		String sum = "";
		int year1 = 0, month1 = 0, day1 = 0;
		int year2 = 0, month2 = 0, day2 = 0;
		int[] monthdays = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		year1 = cal1.get(Calendar.YEAR);
		month1 = cal1.get(Calendar.MONTH);
		day1 = cal1.get(Calendar.DAY_OF_MONTH);
		cal2.setTime(date2);
		year2 = cal2.get(Calendar.YEAR);
		month2 = cal2.get(Calendar.MONTH);
		day2 = cal2.get(Calendar.DAY_OF_MONTH);
		if (IfLeapYear(year1)) {
			monthdays[1] = 29;
		} else {
			monthdays[1] = 28;
		}
		int a = 3, count = 0;
		while (a > 0) {
			if (month1 > monthdays.length - 1) {
				year1++;
				month1 = 0;
			}
			if (IfLeapYear(year1)) {
				monthdays[1] = 29;
			} else {
				monthdays[1] = 28;
			}
			if (day1 > monthdays[month1]) {
				month1++;
				day1 = 1;
			}
			day1++;
			count++;
			if (year1 >= year2 && month1 >= month2 && day1 >= day2) {
				a = -1;

			}
		}
		sum = String.valueOf(count);
		return sum;
	}
	public static boolean IfLeapYear(int year) {// 是否闰年，用于计算时间差
		if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
			return true;
		}
		return false;
	}
	
}


