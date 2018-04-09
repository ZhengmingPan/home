package com.home.common.current;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalContext {
	private static Map<String, Object> contextMap = new HashMap<>();

	public static void put(String key, Object value) {
		contextMap.put(key, value);
	}

	public static <T> T get(String key) {
		return (T) contextMap.get(key);
	}

	public static void reset() {
		contextMap = new HashMap<>();
	}
}