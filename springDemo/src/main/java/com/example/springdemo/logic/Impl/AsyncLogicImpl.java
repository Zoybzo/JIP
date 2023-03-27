package com.example.springdemo.logic.Impl;

import com.example.springdemo.config.SftpConfig;
import com.example.springdemo.logic.AsyncLogic;
import com.example.springdemo.schedule.ScheduleApplication;
import com.example.springdemo.utils.SftpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import static com.example.springdemo.utils.FileUtil.getFilePathForUpload;


@Service
@EnableAsync
public class AsyncLogicImpl implements AsyncLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncLogicImpl.class);

    @Override
    @Async("taskExecutor")
    public void executeAsync() {
        LOGGER.info("start executeAsync");

        System.out.println("异步线程要做的事情");
        System.out.println("可以在这里执行批量插入等耗时的事情");

        LOGGER.info("end executeAsync");
    }

    /**
     * 这块没有直接用异步，而是在定时任务里面用了
     *
     * @param begin
     */
    @Override
//    @Async("taskExecutor")
    public void uploadFilesAsync(int begin) {
        SftpUtil ftp = new SftpUtil(3, 6000);
        try {
            // 加载配置文件信息
            InputStream is = ScheduleApplication.class.getClassLoader().getResourceAsStream("config.properties");
            // 构造 Properties
            Properties properties = new Properties();
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 获取信息
            String ip = properties.getProperty("sftp.ip");
            String username = properties.getProperty("sftp.username");
            String password = properties.getProperty("sftp.password");
            String port = properties.getProperty("sftp.port");
            String timeout = properties.getProperty("sftp.timeout");
            // 服务端目录
            String remoteDir = properties.getProperty("sftp.remotepath");
            // 解密原始数据
            SftpConfig sftpConfig = new SftpConfig(
                    ip,
                    Integer.parseInt(port),
                    username,
                    password,
                    Integer.parseInt(timeout),
                    remoteDir);
            // 上传子文件夹中的文件
            ArrayList<Path> path = getFilePathForUpload(properties.getProperty("sftp.localpath"));
            for (; begin < Objects.requireNonNull(path).size(); begin++) {
                Path filepath = path.get(begin);
                String filepathString = filepath.toString().replace('\\', '/'); // 包含文件名的文件路径
                LOGGER.info("filepathString: " + filepathString);
                String pathWithoutFilename = filepathString.substring(filepathString.lastIndexOf('/') + 1);
                // 按照斜杠划分，把最后一个字符串去掉
                String remoteTargetDirPathString = remoteDir;
                if (filepathString.contains("/")) {
                    int endIndex = filepathString.lastIndexOf("/");
                    String dirPathString = filepathString.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                    LOGGER.info("dirPathString: " + dirPathString);
                    remoteTargetDirPathString = Paths.get(remoteDir, dirPathString).toString().replace('\\', '/');
                }
                LOGGER.info(pathWithoutFilename);
                LOGGER.info("remoteTargetDirPathString: " + remoteTargetDirPathString);
                filepathString = properties.getProperty("sftp.localpath").replace('\\', '/') + '/' + filepathString;
                LOGGER.info("filepathString: " + filepathString);
                ftp.upload(remoteTargetDirPathString, filepathString, sftpConfig);
                LOGGER.info("文件上传成功：[{}]", filepathString);
            }
        } catch (Exception e) {
            LOGGER.error("文件上传下载异常:[{}]", e.getMessage());
            e.printStackTrace();
            uploadFilesAsync(begin);
        }
    }
}