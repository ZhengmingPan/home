package com.home.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * 文件下载工具类
 * 
 * @author Administrator
 *
 */
public class DownloadUtils {

	public static void downloadByFile(File file, String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!file.exists() || !file.canRead()) {
			downloadException(response);
		}
		InputStream input = new FileInputStream(file);
		download(input, fileName, request, response);
	}

	public static void downloadByInputStream(InputStream input, String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (input == null || input.available() == 0) {
			downloadException(response);
		}
		download(input, fileName, request, response);
	}

	private static void download(InputStream input, String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
 		ResponseUtils.setNoCacheHeader(response);
		ResponseUtils.setFileDownloadHeader(response, fileName, String.valueOf(input.available()));
		
 		OutputStream output = response.getOutputStream();
		try {
			// 基于byte数组读取InputStream并直接写入OutputStream, 数组默认大小为4k.
			IOUtils.copy(input, output);
			output.flush();
		} finally {
			if (input != null) {
				// 保证InputStream的关闭.
				IOUtils.closeQuietly(input);
			}
		}
	}

	private static void downloadException(HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write("您下载的文件不存在！");
		throw new Exception("您下载的文件不存在");
	}

}
