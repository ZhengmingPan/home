package com.home.common.current;

public class ThreadContext extends ThreadLocalContext {
	public static final String USER_ID_KEY = "_user_id_key";
	public static final String USER_NAME_KEY = "_user_name_key";
	public static final String IP_KEY = "_ip_key";
	public static final String URL_KEY = "_url_key";
	public static final String DATA_SOURCE_KEY = "_data_source_key";
	public static final String UPLOAD_PATH_KEY = "_upload_path_key";

	public static Long getUserId() {
		return (Long) get("_user_id_key");
	}

	public static void setUserId(Long userId) {
		put("_user_id_key", userId);
	}

	public static String getUserName() {
		return (String) get("_user_name_key");
	}

	public static void setUserName(String userName) {
		put("_user_name_key", userName);
	}

	public static String getIp() {
		return (String) get("_ip_key");
	}

	public static void setIp(String ip) {
		put("_ip_key", ip);
	}

	public static String getUrl() {
		return (String) get("_url_key");
	}

	public static void setUrl(String url) {
		put("_url_key", url);
	}

	public static String getDataSource() {
		return (String) get("_data_source_key");
	}

	public static void setDataSource(String dataSource) {
		put("_data_source_key", dataSource);
	}

	public static String getUploadPath() {
		return (String) get("_upload_path_key");
	}

	public static void setUploadPath(String uploadPath) {
		put("_upload_path_key", uploadPath);
	}
}