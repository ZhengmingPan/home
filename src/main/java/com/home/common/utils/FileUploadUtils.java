package com.home.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.common.io.Files;

/**
 * 文件上传
 * 
 * @author Administrator
 *
 */
public class FileUploadUtils {

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
}
