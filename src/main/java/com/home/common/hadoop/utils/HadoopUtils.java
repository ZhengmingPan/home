package com.home.common.hadoop.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

/**
 * Hadoop  工具类
 */
@Component
public class HadoopUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(HadoopUtils.class);

    private static String PATH_SEPARATOR = "/";

    private static String path;
    private static String username;

    @Value("${hadoop.hdfs.path}")
    private void setPath(String path) {
        HadoopUtils.path = path;
    }

    @Value("${hadoop.hdfs.username}")
    private void setUsername(String username) {
        HadoopUtils.username = username;
    }

    /**
     * 获取HDFS配置信息
     * @return
     */
    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.set(FileSystem.FS_DEFAULT_NAME_KEY, path);
        return configuration;
    }

    /**
     * 获取文件系统对象
     */
    private static FileSystem getFileSystem() {
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(new URI(path), getConfiguration(), username);
        }  catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("HDFS文件系统获取失败");
        }
        return fileSystem;
    }

    /**
     * 创建文件夹
     * @param path 文件夹路径
     */
    public static boolean createFolder(String path) {
        boolean isExist = isExistFolder(path);
        if(!isExist) {
            return false;
        }
        Path targetPath = new Path(path);
        FileSystem fs = getFileSystem();
        boolean isSuccess = false;
        try {
            isSuccess = fs.mkdirs(targetPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(!isSuccess) {
                LOGGER.error("HDFS创建文件夹失败");
            }
            close(fs);
        }
        return isSuccess;
    }

    /**
     * 删除文件夹
     * @param path
     * @return
     */
    public static boolean deleteFolder(String path) {
        boolean isExist = isExistFolder(path);
        if(!isExist) {
            return false;
        }
        boolean isSuccess = false;
        FileSystem fs = getFileSystem();
        Path targetPath = new Path(path);
        try {
            isSuccess = fs.delete(targetPath, true);
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(!isSuccess) {
                LOGGER.error("HDFS删除文件夹失败");
            }
            close(fs);
        }
        return isSuccess;
    }

    /**
     * 重命名文件夹
     * @param path
     * @param newName
     * @return
     */
    public static boolean renameFolder(String path, String newName) {
        boolean isExist = isExistFolder(path);
        if(!isExist) {
            return false;
        }
        boolean isSuccess = false;
        Path sourcePath = new Path(path);
        Path targetPath = new Path(StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, PATH_SEPARATOR) + 1) + newName);
        FileSystem fs = getFileSystem();
        try {
            isSuccess = fs.rename(sourcePath, targetPath);
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(!isSuccess) {
                LOGGER.error("HDFS重命名文件夹失败");
            }
            close(fs);
        }
        return isSuccess;
    }

    /**
     * 判断文件夹是否存在
     * @param path
     * @return
     */
    public static boolean isExistFolder(String path) {
        if(StringUtils.isBlank(path)) {
            return false;
        }

        FileSystem fs = getFileSystem();
        if(fs == null) {
            return false;
        }
        Path targetPath = new Path(path);
        boolean isExist = false;
        try {
            isExist = fs.exists(targetPath);
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(!isExist) {
                LOGGER.error("文件夹不存在");
            }
            close(fs);
        }
        return isExist;
    }

    /**
     * 关闭文件系统
     * @param fileSystem  文件系统对象
     */
    private static void close(FileSystem fileSystem) {
        if(fileSystem != null) {
            try {
                fileSystem.close();
            } catch (IOException e) {
                LOGGER.error("HDFS文件系统关闭异常");
                e.printStackTrace();
            }
        }
    }


}
