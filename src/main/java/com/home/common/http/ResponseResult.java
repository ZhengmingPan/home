package com.home.common.http;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
	
@SuppressWarnings("serial")
@Getter
@Setter
@ToString(callSuper = true)
public class ResponseResult<T>  implements Serializable {

	private Integer code; // 状态码

	private Boolean success; // 是否成功

	private String message; // 错误信息

	private T data; // 返回值

	public static final ResponseResult<?> SUCCEED = new ResponseResult<>();
	public static final ResponseResult<?> BAD_REQUEST = new ResponseResult<>(HttpStatus.BAD_REQUEST);
	public static final ResponseResult<?> UNAUTHORIZED = new ResponseResult<>(HttpStatus.UNAUTHORIZED);
	public static final ResponseResult<?> FORBIDDEN = new ResponseResult<>(HttpStatus.FORBIDDEN);
	public static final ResponseResult<?> INTERNAL_SERVER_ERROR = new ResponseResult<>(HttpStatus.INTERNAL_SERVER_ERROR);
 

	public static <T> ResponseResult<T> createSuccess(T data) {
		return new ResponseResult<>(data);
	}
	
	public static <T> ResponseResult<T> createParamError(String message) {
		return new ResponseResult<>(HttpStatus.BAD_REQUEST, message);
	}

	public static <T> ResponseResult<T> createError(HttpStatus httpStatus, String message) {
		return new ResponseResult<>(httpStatus, message);
	}
	
	private ResponseResult() {
		this.success = true;
		this.code = HttpStatus.OK.value();
		this.message = HttpStatus.OK.getReasonPhrase();
	}

	private ResponseResult(T data) {
		this.success = true;
		this.code = HttpStatus.OK.value();
		this.message = HttpStatus.OK.getReasonPhrase();
		this.data = data;
	}

	private ResponseResult(HttpStatus httpStatus) {
		this.success = false;
		this.code = httpStatus.value();
		this.message = httpStatus.getReasonPhrase();
	}

	private ResponseResult(HttpStatus httpStatus, String message) {
		this.success = false;
		this.code = httpStatus.value();
		this.message = message;
	}

}
