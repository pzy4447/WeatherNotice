package com.pzy;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author lenovo 使用quartz定期执行任务
 */
public class WeatherNoticer {

	Date execTime = new GregorianCalendar(0, 0, 0, 10, 0).getTime();// //
	String apikey;
	Map<String, List<String>> subscriptionMap; // 程序运行时间,默认为10点整

	public String getApikey() {
		return apikey;
	}

	public Map<String, List<String>> getSubscriptionMap() {
		return subscriptionMap;
	}

	private void init() throws Throwable {
		// 加载配置文件
		Properties pps = new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream(
				"config.properties"));
		pps.load(in);
		// 提取运行时间
		String execTimeString = pps.getProperty("execTime");
		if (execTimeString != null) {
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			try {
				execTime = df.parse(execTimeString);
				System.out.printf("%s%n", execTime.toString());
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("config.properties中的execTime值无法解析:"
						+ execTime);
			}
		}
		// 提取apikey,用于发送短信
		apikey = pps.getProperty("apikey");
		if (apikey == null) {
			throw new Exception("config.properties中找不到apikey%n");
		}
		// 剔除系统配置，剩下的就是订阅配置
		HashSet<Object> keySet = new HashSet<Object>(pps.keySet());
		keySet.remove("execTime");
		keySet.remove("apikey");
		subscriptionMap = new HashMap<String, List<String>>();
		for (Object obj : keySet) {
			String key = (String) obj;
			String value = pps.getProperty(key);
			List<String> phoneList = Arrays.asList(value.split(","));
			subscriptionMap.put(key, phoneList);
			System.out.printf("%s,%s%n", key, phoneList);
		}
	}

	private void run() {
		// 获取当前时间
		Calendar c = Calendar.getInstance();
		// System.out.printf("%s%n", c.getTime().toString());
		// 将时间设定在20点整
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), 11, 50, 0);
		// System.out.printf("%s%n", c.getTime().toString());
		// 设定触发器.startAt(c.getTime())
		Trigger trigger = newTrigger()
				.startNow()
				.withSchedule(
						calendarIntervalSchedule().withInterval(5,
								IntervalUnit.DAY)).build();
		// 设定任务
		JobDetail job1 = newJob(MyJob.class).withIdentity("job1", "group1")
				.build();

		// 设定调度器
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = null;
		try {
			sched = sf.getScheduler();
			Date ft = sched.scheduleJob(job1, trigger);
			// System.out.printf("%s%n", trigger.getStartTime());
			sched.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void timeTest() throws Throwable {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date date = df.parse("20:01:05");

		Calendar c = new GregorianCalendar();
		c.setTime(date);
		System.out.printf("%s%n", c.get(Calendar.HOUR_OF_DAY));
		System.out.printf("%s%n", c.get(Calendar.MINUTE));
		System.out.printf("%s%n", c.get(Calendar.SECOND));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WeatherNoticer wn = new WeatherNoticer();
		try {
			// wn.timeTest();
			wn.init();
			// wn.run();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			System.err.printf("%s%n", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
}
