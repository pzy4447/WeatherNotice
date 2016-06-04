package com.pzy;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Weather {

	Date date;

	String weather;
	int tempratureHigh;
	int tempratureLow;
	String windPower;
	String windDirection;

	public Weather(String weather, int tempratureHigh, int tempratureLow,
			String windPower, String windDirection) {
		super();
		this.weather = weather;
		this.tempratureHigh = tempratureHigh;
		this.tempratureLow = tempratureLow;
		this.windPower = windPower;
		this.windDirection = windDirection;
	}

	public Weather(Date date, String weather, int tempratureHigh,
			int tempratureLow, String windPower, String windDirection) {
		this(weather, tempratureHigh, tempratureLow, windPower, windDirection);
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getWeather() {
		return weather;
	}

	public int getTempratureHigh() {
		return tempratureHigh;
	}

	public int getTempratureLow() {
		return tempratureLow;
	}

	public String getWindPower() {
		return windPower;
	}

	public String getWindDirection() {
		return windDirection;
	}

	@Override
	public String toString() {
		String dateString = DateFormat.getDateInstance(DateFormat.DEFAULT)
				.format(date);
		return "[天气简报]日期=" + dateString + ", 天气=" + weather + ", 最高气温="
				+ tempratureHigh + "℃, 最低气温=" + tempratureLow + ",风力="
				+ windPower + ", 风向=" + windDirection;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar tomorrowCalendar = Calendar.getInstance();
		tomorrowCalendar.add(Calendar.DATE, 27);
		System.out.printf("%s%n", tomorrowCalendar.getTime().toString());
	}

}
