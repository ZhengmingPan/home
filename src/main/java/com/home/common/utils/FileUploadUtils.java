package com.home.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 文件上传
 * 
 * @author Administrator
 *
 */
@Component
public class FileUploadUtils {

	/**
	 * 读取配置项（upload.fastdfs.enabled）是否启用FastDFS分布式文件系统
	 */
	private static boolean isUseFastDFS = false;

	@Value("${upload.fastdfs.enabled}")
	public void setIsUseFastDFS(boolean isUseFastDFS) {
		FileUploadUtils.isUseFastDFS = isUseFastDFS;
	}

	/**
	 * 将文件存储只本地系统并返回存储路径
	 * @param data   文件字符编码形式存储
	 * @param root   根目录
	 * @param path   文件夹
	 * @param fileType  文件类型后缀
	 * @return
	 */
	public static String storeFile(byte[] data, String root, String path, String fileType) {

		String parentPath = path + "/" + DateFormatUtils.format(new Date(), "yyyyMMdd");
		File parent = new File(root, parentPath);
		if (!parent.exists()) {
			parent.mkdirs();
		}
		String fileName = Calendar.getInstance().getTimeInMillis() + "." + fileType;
		String savePath = parentPath + "/" + fileName;
		File target = new File(root, savePath);
		if (!target.exists()) {
			try {
				target.createNewFile();
			} catch (IOException e) {
			}
		}
		try {
			Files.write(data, target);
		} catch (IOException e) {

		}
		return savePath;
	}


	/**
	 * 文件上传兼容分布式文件系统
	 * @param data
	 * @param root
	 * @param path
	 * @param fileType
	 * @param fileName
	 * @return
	 */
	public static Map<String, String> storeFile(byte[] data, String root, String path, String fileType, String fileName) {
		Map<String, String> pathmap = Maps.newHashMap();
		String filepath = storeFile(data, root, path, fileType);
		pathmap.put("localsystem", filepath);
		if(isUseFastDFS) {
			String fastdfsUrl = FastDFSUtils.uploadByFileByte(fileName, data);
			pathmap.put("fastdfsurl", fastdfsUrl);
		}
		return pathmap;
	}
}
