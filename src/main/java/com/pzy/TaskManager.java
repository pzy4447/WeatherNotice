package com.pzy;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Calendar;
import java.util.Date;

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
public class TaskManager {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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

}
