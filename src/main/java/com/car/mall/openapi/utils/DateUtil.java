package com.car.mall.openapi.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期时间工具类
 * 
 * @author R19343
 * 
 */
public class DateUtil {
	/**
	 * 字符串转换日期
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param format
	 *            日期格式
	 * @return
	 * @throws ParseException
	 */
	
	public static Map<Integer,String> dayWeek ;
	
	static{
		dayWeek = new HashMap<Integer,String>();
		dayWeek.put(1, "周一");
		dayWeek.put(2, "周二");
		dayWeek.put(3, "周三");
		dayWeek.put(4, "周四");
		dayWeek.put(5, "周五");
		dayWeek.put(6, "周六");
		dayWeek.put(7, "周日");
		
	}
	public static Date String2Date(String dateStr, String format)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}

	public static String Date2String(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 对当前系统时间增加i小时
	 * @param i
	 * @return
	 */
	public static Date getAddCurrentTime(int i){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, i);
		return c.getTime();
	}

	/**
	 * 对当前系统时间增加i天
	 * @param i
	 * @return
	 */
	public static Date getAddCurrentDay(int i){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, i);
		return c.getTime();
	}

	/**
	 * 秒-小时
	 * @param str
	 * @return
	 */
	public static int getTimerForHore(String str){
		
		return (int) Math.ceil((Integer.parseInt(str)/3600));
	}
	
    public static int daysInMonth(final int year, final int month){
        int lastDayOfMonth = 0;
        if(month==4 || month==6 || month==9 || month==11){
            lastDayOfMonth = 30;
        }else if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
            lastDayOfMonth = 31;
        }else if(month == 2){
            if(year%400==0 || (year%4==0 && year%100==0))
                lastDayOfMonth = 29;
            else
                lastDayOfMonth = 28;
        }

        return lastDayOfMonth;
    }
	
	public static List<String> getRecentMonth(final int num){
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
		Calendar calendar;
		List<String> list = new ArrayList<String>();
		for(int index=0;index<num;index++){
			calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-index);
			list.add(format.format(calendar.getTime()));
		}
		
		return list;
	}
	
	public static List<String> getRecentYear(final int num){
		final SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Calendar calendar;
		List<String> list = new ArrayList<String>();
		for(int index=0;index<num;index++){
			calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-index);
			list.add(format.format(calendar.getTime()));
		}
		
		return list;		
	}
	
	/**
	 * 判断当前日期是星期几
	 * 
	 * @param pTime
	 *            修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}
	
	public static String dayForWeek2(Date d) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayWeek.get(dayForWeek);
	}
	
	
	
	public static String showDayName(String dateStr) throws Exception{
		
		Calendar   cal2   =   Calendar.getInstance();
		String today = new SimpleDateFormat("yyyy-MM-dd").format(cal2.getTime());
		if(today.equals(dateStr)){
			return "今天";
		}
		
		Calendar   cal3   =   Calendar.getInstance();
		cal3.add(Calendar.DATE,   +1);
		String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(cal3.getTime());
		if(tomorrow.equals(dateStr)){
			return "明天";
		}
		
		
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,   -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		if(yesterday.equals(dateStr)){
			return "昨天";
		}
		return "";
		
	}

	public static Date getStartTime() {
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.HOUR_OF_DAY, 00);
		todayStart.set(Calendar.MINUTE, 00);
		todayStart.set(Calendar.SECOND, 00);
		return todayStart.getTime();
	}

	public static Date getEndTime() {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		return todayEnd.getTime();
	}

	public static ArrayList<String> getSoaBillDateList(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> dateList = new ArrayList<>();
		Date todayDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(todayDate);
		long time2018 = 1527696000000L;
		StringBuilder sb = new StringBuilder();
		while(todayDate.getTime()>time2018){
			String timeStr = format.format(todayDate);
			String[] split = timeStr.split("-");
			if("01".equals(split[2]) || "16".equals(split[2])){
				sb.append(split[0]);
				sb.append("年");
				sb.append(split[1]);
				sb.append("月");
				if("01".equals(split[2])){
					sb.append("上期");
				}else {
					sb.append("下期");
				}
//				System.out.println(sb.toString());
				dateList.add(sb.toString());
				sb = sb.delete(0, sb.length());
			}
			calendar.add(Calendar.DATE, -1);
			todayDate = calendar.getTime();
		}
		dateList.remove(0);
		return dateList;
	}

	public static void main(String[] args) {
		Date date = DateUtil.getStartTime();
		System.out.println(date.toString());
		System.out.println(Date2String(date,"yyyy-MM-dd HH:mm:ss"));
		System.out.println(Date2String(DateUtil.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
	}


}
