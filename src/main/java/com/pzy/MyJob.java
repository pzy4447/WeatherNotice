package com.pzy;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import us.codecraft.webmagic.Spider;

/**
 * @author lenovo 执行任务：抓取数据，分析数据，消息提醒
 */
public class MyJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		WeatherPageProcessor wpp = new WeatherPageProcessor();
		wpp.setPhoneNumber("13669198711");
		// 抓取数据
		String url1 = WeatherPageProcessor
				.getCity7DaysWeatherPageUrl("101110101");
		Spider spider = Spider.create(wpp).addUrl(url1);
		spider.run();
		// 处理数据
		// spider.stop();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WeatherPageProcessor wpp = new WeatherPageProcessor();
		wpp.setPhoneNumber("13669198711");
		// 抓取数据
		String url1 = WeatherPageProcessor
				.getCity7DaysWeatherPageUrl("101110101");
		Spider spider = Spider.create(new WeatherPageProcessor()).addUrl(url1);
		spider.run();
	}
}
