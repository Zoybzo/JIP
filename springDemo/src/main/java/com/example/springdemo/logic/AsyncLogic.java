package com.example.springdemo.logic;

public interface AsyncLogic {

    /**
     * 执行异步任务 * 可以根据需求，自己加参数拟定
     */
    void executeAsync();

    void uploadFilesAsync(int begin);
}
