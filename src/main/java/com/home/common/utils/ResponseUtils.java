package com.home.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;

/**
 * Response请求返回值工具类
 * 
 * @author Administrator
 *
 */
public class ResponseUtils {

	// 请求输出json
	public static void renderJson(HttpServletResponse response, Object data) {
		// 设置编码格式
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(JSON.toJSONString(data));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setNoCacheHeader(HttpServletResponse response) {
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}

	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		String encodedfileName = new String(fileName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
	}

	public static void setFileDownloadHeader(HttpServletResponse response, String fileName, String fileLength) {
		String encodedfileName = new String(fileName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
		response.setHeader("Content-Length", fileLength);
	}

	public static void download(File file, String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileType = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ENGLISH);
		// ---------------------------------------------------------------
		// 设置MIME
		// ---------------------------------------------------------------
		if (fileType.equalsIgnoreCase("xls") || fileType.equalsIgnoreCase("xlsx")) {
			response.setContentType("application/vnd.ms-excel");
		} else if (fileType.equalsIgnoreCase("doc")) {
			response.setContentType("application/msword");
		} else if (fileType.equalsIgnoreCase("ppt")) {
			response.setContentType("application/vnd.ms-powerpoint");
		} else if (fileType.equalsIgnoreCase("pdf")) {
			response.setContentType("application/pdf");
		} else if (fileType.equalsIgnoreCase("gif")) {
			response.setContentType("image/gif");
		} else if (fileType.equalsIgnoreCase("jpg") || fileType.equalsIgnoreCase("jpeg")) {
			response.setContentType("image/jpeg");
		} else if (fileType.equalsIgnoreCase("png")) {
			response.setContentType("image/png");
		} else if (fileType.equalsIgnoreCase("bmp")) {
			response.setContentType("image/x-xbitmap");
		} else if (fileType.equalsIgnoreCase("svg")) {
			response.setContentType("image/svg+xml");
		} else if (fileType.equalsIgnoreCase("mp3")) {
			response.setContentType("audio/mp3");
		} else if (fileType.equalsIgnoreCase("aac")) {
			response.setContentType("audio/mp4");
		} else if (fileType.equalsIgnoreCase("mp4")) {
			response.setContentType("video/mpeg4");
		} else if (fileType.equalsIgnoreCase("zip")) {
			response.setContentType("application/zip");
		} else {
			response.setContentType("multipart/form-data");
		}

		if (!file.exists() || !file.canRead()) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("您下载的文件不存在！");
			return;
		}

		response.setContentType("application/octet-stream"); 
		response.reset(); 
		ResponseUtils.setNoCacheHeader(response);
		ResponseUtils.setFileDownloadHeader(response, fileName, String.valueOf(file.length()));

		InputStream input = null;
		OutputStream output = response.getOutputStream();
		try {
			input = new FileInputStream(file);
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

}
