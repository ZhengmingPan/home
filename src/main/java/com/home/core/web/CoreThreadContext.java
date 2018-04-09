package com.home.core.web;

import com.home.common.current.ThreadContext;
import com.home.core.entity.BaseUser;

public class CoreThreadContext extends ThreadContext {

	public static final String USER_KEY = "_user_key";

	public static void setUser(BaseUser user) {
		setUserId(user.getId());
		setUserName(user.getName());
		put(USER_KEY, user);
	}

	public static BaseUser getUser() {
		return (BaseUser) get(USER_KEY);
	}
}
