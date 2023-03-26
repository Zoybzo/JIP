package com.example.springdemo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static ArrayList<Path> getFilePathForUpload(String path) {
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return null;
        }

        ArrayList<Path> res = new ArrayList<>();

        File dirs[] = f.listFiles();
        for (File dir : dirs) {
            if (dir.isDirectory()) {
                LOGGER.debug(dir.getName() + " [目录]");
                File[] files = dir.listFiles();
                for (File file : files) {
                    res.add(Paths.get(dir.getName(), file.getName()));
                }
            } else {
                LOGGER.error("不应该存在的文件：" + dir.getName());
            }
        }
        return res;
    }
}
