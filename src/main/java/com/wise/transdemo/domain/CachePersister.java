package com.wise.transdemo.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author wise
 *
 * 提供文件持久化内存实现
 */

public class CachePersister {

    private final ObjectMapper mapper = new ObjectMapper();
    private final File file;
    private final ReentrantLock lock = new ReentrantLock();

    public CachePersister(String filePath) {
        this.file = new File(filePath);
        // 确保目录存在
//        if (!file.getParentFile().exists()) {
//            file.getParentFile().mkdirs();
//        }
    }

    // 线程安全的加载方法
    @SuppressWarnings("unchecked")
    public Map<String, Transaction> load() {
        lock.lock();
        try {

            if (file.exists() && file.length() > 0) {
                //  notice  Java 序列化的泛型擦除
                JavaType type = mapper.getTypeFactory().constructMapType(
                        ConcurrentHashMap.class,
                        String.class,
                        Transaction.class
                );
                return mapper.readValue(file, type);
            }
            return new ConcurrentHashMap<>();
        } catch (IOException e) {
            System.err.println("加载缓存失败: " + e.getMessage());
            return new ConcurrentHashMap<>();
        } finally {
            lock.unlock();
        }
    }

    // 线程安全的保存方法
    public void save(Map<?, ?> cache) {
        lock.lock();
        try {
            // 使用临时文件避免写操作中断导致的数据损坏
            File tempFile = new File(file.getAbsolutePath() + ".tmp");
            mapper.writeValue(tempFile, cache);

            // 原子替换
            if (tempFile.exists()) {
                if (file.exists() && !file.delete()) {
                    System.err.println("无法删除旧缓存文件");
                }
                if (!tempFile.renameTo(file)) {
                    System.err.println("无法重命名临时缓存文件");
                }
            }
        } catch (IOException e) {
            System.err.println("保存缓存失败: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
