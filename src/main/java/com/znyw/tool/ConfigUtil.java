package com.znyw.tool;

public class ConfigUtil {
	public final static PropertyConfigUtil propertyconfig=PropertyConfigUtil.getInstance("properties/config.properties");
	private static String ROOT = "0310";

	public static void setRoot(String id) {
		if (id != null) {
			ROOT = id;
		}
	}

	public static String getRoot() {
		return ROOT;
	}
}
