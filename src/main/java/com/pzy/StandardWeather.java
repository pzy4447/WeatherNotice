package com.pzy;

/**
 * @author lenovo 将各种杂乱的天气标准化，比如将多云转晴视为晴
 */
public enum StandardWeather {
	晴, 阴, 雨, 雪, 其它;

	public static StandardWeather getStandardWeather(String weather) {
		StandardWeather sw = null;
		if (weather.contains("雨"))
			sw = 雨;
		else if (weather.contains("雪")) {
			sw = 雪;
		} else if (weather.contains("阴")) {
			sw = 阴;
		} else if (weather.contains("晴") || (weather.contains("云"))) {
			sw = 晴;
		} else {
			sw = 其它;
		}
		return sw;
	}
}
