package com.pzy;

import java.util.Calendar;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class WeatherPageProcessor implements PageProcessor {
	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(0)
			.setCharset("utf-8").setTimeOut(60000);

	static String mainSiteUrl = "http://www.weather.com.cn";
	String phoneNumber;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public WeatherPageProcessor() {
		super();
	}

	public static String getCityTodayWeatherPageUrl(String cityId) {
		return mainSiteUrl + "/weather1d/" + cityId + ".shtml";
	}

	public static String getCity7DaysWeatherPageUrl(String cityId) {
		return mainSiteUrl + "/weather/" + cityId + ".shtml";
	}

	public void process(Page page) {
		// TODO Auto-generated method stub
		process7DaysPage(page);
	}

	public void process7DaysPage(Page page) {
		// url为http://www.weather.com.cn/weather/101110101.shtml，提供7天的天气
		Html html = page.getHtml();
		// System.out.printf("html is %s%n", html);
		// 提取天气信息块，包括今天与明天
		String weatherInfoXPathExp = "//ul[@class='t clearfix']";
		String weatherInfo = html.xpath(weatherInfoXPathExp).get();
		// System.out.printf("weatherInfo is :%n%s%n", weatherInfo);

		// 提取今天天气
		Html tomorrowHtml = new Html(weatherInfo);
		String ss1 = tomorrowHtml.xpath("ul/li[1]").get();
		Weather todayWeather = getWeather(ss1);
		Calendar todayCalendar = Calendar.getInstance();
		todayWeather.setDate(todayCalendar.getTime());
		// 提取明天天气
		String ss2 = tomorrowHtml.xpath("ul/li[2]").get();
		Weather tomorrowWeather = getWeather(ss2);
		Calendar tomorrowCalendar = Calendar.getInstance();
		tomorrowCalendar.add(Calendar.DATE, 1);
		tomorrowWeather.setDate(tomorrowCalendar.getTime());

		// 处理数据
		handleWeather(todayWeather, tomorrowWeather);
	}

	public void processTodayPage(Page page) {
		// url为http://www.weather.com.cn/weather1d/101110101.shtml，提供今天白天与夜间，或今天夜间与明天白天的天气
		Html html = page.getHtml();
		// System.out.printf("html is %s%n", html);
		// 提取天气信息块，包括今天与明天
		String weatherInfoXPathExp = "//div[@class='today clearfix']/div/ul[@class='clearfix']";
		String weatherInfo = html.xpath(weatherInfoXPathExp).get();
		System.out.printf("weatherInfo is :%n%s%n", weatherInfo);

		// 提取今天天气
		Html tomorrowHtml = new Html(weatherInfo);
		String ss1 = tomorrowHtml.xpath("ul/li[1]").get();
		Weather todayWeather = getWeather(ss1);
		page.putField("today", todayWeather);
		// 提取明天天气
		String ss2 = tomorrowHtml.xpath("ul/li[2]").get();
		Weather tomorrowWeather = getWeather(ss2);
		page.putField("tomorrow", tomorrowWeather);
	}

	private Weather getWeather(String htmlString) {
		Html html = new Html(htmlString);
		// System.out.printf("html is %s%n", html);
		String weather = html.xpath("//li/p[@class='wea']/text()").get();
		String temperatureHighString = html.xpath(
				"//li/p[@class='tem']/span/text()").get();
		// 最高气温
		int temperatureHigh = 1000;
		if (temperatureHighString != null) {
			temperatureHighString = StringUtils.getMatchedString("(\\w*)",
					temperatureHighString);
			try {
				temperatureHigh = Integer.parseInt(temperatureHighString);
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("无法解析温度值：" + temperatureHighString);
			}
		}
		// 最低气温
		int temperatureLow = -1000;
		String temperatureLowString = html.xpath(
				"//li/p[@class='tem']/i/text()").get();
		if (temperatureHighString != null) {
			temperatureLowString = StringUtils.getMatchedString("(\\w*)",
					temperatureLowString);
			try {
				temperatureLow = Integer.parseInt(temperatureLowString);

			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("无法解析温度值：" + temperatureLowString);
			}
		}

		String windDirection = html.xpath(
				"//li/p[@class='win']/em/span[1]/@title").get();
		String windPower = html.xpath("//li/p[@class='win']/i/text()").get();
		Weather w = new Weather(weather, temperatureHigh, temperatureLow,
				windPower, windDirection);
		return w;
	}

	public void handleWeather(Weather todayWeather, Weather tomorrowWeather) {
		StandardWeather todaySW = StandardWeather
				.getStandardWeather(todayWeather.getWeather());
		StandardWeather tomorrowSW = StandardWeather
				.getStandardWeather(tomorrowWeather.getWeather());

		// 处理数据并消息提醒
		if (tomorrowSW.equals(StandardWeather.其它)) {
			// System.out.printf("天气未正确识别%n");
			notice(tomorrowWeather.toString() + "[天气未正确识别]");
			return;
		}
		// 天气有变
		if (!todaySW.equals(tomorrowSW)) {
			notice(tomorrowWeather.toString() + "[" + tomorrowSW.toString()
					+ "]");
			return;
		} else {// 天气未变
			System.out.printf("天气未变%n");
		}

		int tempDiff = todayWeather.getTempratureHigh()
				- tomorrowWeather.getTempratureHigh();
		if (tempDiff > 3 || tempDiff < -3) {
			// System.out.printf("气温剧变%n");
			notice(tomorrowWeather.toString() + "[气温剧变]");
			return;
		}
	}

	private void notice(String msg) {
		System.out.printf("msg : %s%n", msg);
	}

	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		String url1 = WeatherPageProcessor
				.getCity7DaysWeatherPageUrl("101110101");
		Spider spider = Spider.create(new WeatherPageProcessor()).addUrl(url1);
		spider.run();

	}

}
