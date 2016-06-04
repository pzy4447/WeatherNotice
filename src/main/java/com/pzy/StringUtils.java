package com.pzy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String temperatureLowString = "20℃";
		temperatureLowString = StringUtils.getMatchedString("(\\w*)",
				temperatureLowString);
		System.out.printf("%s%n", temperatureLowString);
	}

	public static String getMatchedString(String rex, String content) {
		String matchedString = null;
		Pattern pattern = Pattern.compile(rex);
		Matcher matcher = pattern.matcher(content);
		// System.out.printf("groupCount is %d%n", matcher.groupCount());
		if (matcher.find()) {
			/*
			 * for (int i = 0; i < matcher.groupCount(); i++) {
			 * System.out.printf("%s%n", matcher.group(i)); }
			 */
			matchedString = matcher.group(1);
		}
		return matchedString;
	}

}
