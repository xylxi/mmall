package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TokenCache {

    private static Logger log = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    private static LoadingCache<String, String> localCache = CacheBuilder
            .newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                // 数据加载实现，当调用 get 获取值的时候，如果没有值，则会调用这块方法
                @Override
                public String load(String key) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    public static String getKey(String key) {
        try {
            String value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("localCache get error", e);
        }
        return null;
    }

}
