package com.home.core.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.home.core.vo.FastDFSFile;

public class FastDFSClient {

	private static Logger LOGGER = LoggerFactory.getLogger(FastDFSClient.class);

	private static TrackerClient trackerClient;
	private static TrackerServer trackerServer;
	private static StorageClient storageClient;
	private static StorageServer storageServer;

	static {
		try {
			String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
			ClientGlobal.init(filePath);
			trackerClient = new TrackerClient();
			trackerServer = trackerClient.getConnection();
			storageServer = trackerClient.getStoreStorage(trackerServer);
		} catch (Exception e) {
			LOGGER.error("FastDFS Client Init Fail!", e);
		}
	} 

	public static String[] upload(FastDFSFile file) throws Exception {
		LOGGER.info("File Name: " + file.getName() + ", File Length: " + file.getContent().length);
		;
		NameValuePair[] meta_list = new NameValuePair[1];
		meta_list[0] = new NameValuePair("author", file.getAuthor());

		long startTime = System.currentTimeMillis();
		String[] uploadResults = null;
		try {
			storageClient = new StorageClient(trackerServer, storageServer);
			uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
		} catch (IOException e) {
			LOGGER.error("IO Exception when uploading the file: " + file.getName(), e);
		    throw new IOException();
		} catch (Exception e) {
			LOGGER.error("Non IO Exception when uploading the file: " + file.getName(), e);
		    throw new Exception();
		}
		LOGGER.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + "ms");

		if (uploadResults == null) {
			LOGGER.error("upload file fail, error code: " + storageClient.getErrorCode());
		}
		String groupName = uploadResults[0];
		String remoteFileName = uploadResults[1];

		LOGGER.info("upload file successfully !! group_name" + groupName + ", remotefileName: " + remoteFileName);
		return uploadResults;
	}

	public static FileInfo getFile(String groupName, String remoteFileName) {
		try {
			storageClient = new StorageClient(trackerServer, storageServer);
			return storageClient.get_file_info(groupName, remoteFileName);
		} catch (IOException e) {
			LOGGER.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			LOGGER.error("IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	public static InputStream downloadFile(String groupName, String remoteFileName) {
		try {
			storageClient = new StorageClient(trackerServer, storageServer);
			byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
			InputStream ins = new ByteArrayInputStream(fileByte);
			return ins;
		} catch (IOException e) {
			LOGGER.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			LOGGER.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	public static void deleteFile(String groupName, String remoteFileName) throws Exception {
		storageClient = new StorageClient(trackerServer, storageServer);
		int i = storageClient.delete_file(groupName, remoteFileName);
		LOGGER.info("delete file successfully!!!" + i);
	}

	public static StorageServer[] getStoreStorages(String groupName) throws IOException {
		return trackerClient.getStoreStorages(trackerServer, groupName);
	}

	public static ServerInfo[] getFetchStorages(String groupName, String remoteFileName) throws IOException {
		return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
	}

	public static String getTrackerUrl() {
		return "http://" + trackerServer.getInetSocketAddress().getHostString() + ":"
				+ ClientGlobal.getG_tracker_http_port() + "/";
	}

}
