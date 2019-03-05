package com.home.common.utils;

import com.home.common.utils.prop.FastDFSFile;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * FastDFS分布式文件上传和下载客户端工具类
 * 
 * @author Administrator
 *
 */
public class FastDFSUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(FastDFSUtils.class);

	private static TrackerClient trackerClient;
	private static TrackerServer trackerServer;
	private static StorageClient storageClient;
	private static StorageServer storageServer;

	/**
	 * 建立于FastDFS服务器连接
	 */
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

	/**
	 * 将文件信息装换为<code>FastDFSClient</code>并文件通过字节码上传
	 * @param fileName  文件名
	 * @param data  文件流字节码
	 * @return
	 * @throws Exception
	 */
	public static String uploadByFileByte(String fileName, byte[] data) {
		String fileType = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ENGLISH);
		FastDFSFile file = new FastDFSFile(fileName, data, fileType);
		try {
			return uploadFile(file);
		} catch (Exception e) {
			LOGGER.error("FastDFS 文件上传失败");
			return null;
		}
	}
 
	/**
	 * 文件上传值FastDFS
	 * @param file
	 * @return 文件在FastDFS上的全路径
	 * @throws Exception
	 */
	private static String uploadFile(FastDFSFile file) throws Exception {
		LOGGER.info("File Name: " + file.getName() + ", File Length: " + file.getContent().length); 
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
			throw new Exception();
		}
		String groupName = uploadResults[0];
		String remoteFileName = uploadResults[1];
		LOGGER.info("upload file successfully !! group_name" + groupName + ", remotefileName: " + remoteFileName);

		return getTrackerUrl() + groupName + "/" + remoteFileName;
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

	/**
	 * 根据FastDFS文件地址下载获取输入流
	 * @param fastdfsUrl
	 * @return
	 */
	public static InputStream downloadFileByUrl(String fastdfsUrl) {
		String path = fastdfsUrl.substring(fastdfsUrl.indexOf("group"));
		String groupName = path.split("/")[0];
		String remoteFileName = path.substring(path.indexOf("/") + 1);
		return downloadFile(groupName, remoteFileName);
	}

	private static InputStream downloadFile(String groupName, String remoteFileName) {
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

	/**
	 * 将文件从FastDFS服务器上删除
	 * @param fastdfsUrl
	 * @throws Exception
	 */
	public static void deleteFileByUrl(String fastdfsUrl) throws Exception {
		String path = fastdfsUrl.substring(fastdfsUrl.indexOf("group"));
		String groupName = path.split("/")[0];
		String remoteFileName = path.substring(path.indexOf("/") + 1);
		deleteFile(groupName, remoteFileName);
	}
	
	private static void deleteFile(String groupName, String remoteFileName) throws Exception {
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

	private static String getTrackerUrl() {
		return "http://" + trackerServer.getInetSocketAddress().getHostString() + ":" + ClientGlobal.getG_tracker_http_port() + "/";
	}

}
