package com.clayf.corehelper.helper;

import com.clayf.corehelper.exception.CustomizedInterruptedException;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * http 工具类
 * <p>
 * 使用okhttp作为底层做工具方法封装
 * <p>
 * created by f at 8/9/21 19:54
 */
public final class OkhttpRequestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkhttpRequestHelper.class);
    /**
     * 请求日志级别配置
     */
    public static final HttpLoggingInterceptor LOGGING_INTERCEPTOR = new HttpLoggingInterceptor(LOGGER::info).setLevel(HttpLoggingInterceptor.Level.BASIC);
    /**
     * 每次请求间隔，单位毫秒
     */
    private static final int REQUEST_DELAY_TIME = 700;

    private OkhttpRequestHelper() {
    }


    /**
     * get 请求
     *
     * @param client client
     * @param headers   请求头
     * @param requestUrl 请求url
     * @param func 请求结果处理函数
     * @param <T> 返回类型
     * @return t
     */
    public static <T> T get(OkHttpClient client, Headers headers, String requestUrl, BiFunction<String, String, T> func) {
        return syncRequest(client, new Request.Builder().url(requestUrl).headers(headers).build(), func);
    }

    /**
     *  post请求
     * @param client client
     * @param headers 请求头
     * @param requestUrl url
     * @param data 内容
     * @param func 请求结果处理函数
     * @param <T> 返回类型
     * @return t
     */
    public static <T> T post(OkHttpClient client, Headers headers, String requestUrl, Map<String, String> data, BiFunction<String, String, T> func) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> item : data.entrySet())
            formBodyBuilder.add(item.getKey(), item.getValue());
        return syncRequest(client, new Request.Builder().url(requestUrl).headers(headers).post(formBodyBuilder.build()).build(), func);
    }

    /**
     * 同步发送请求
     *
     * @param client  client
     * @param request request
     * @param func    process response string, url return T
     * @param <T>     return value
     * @return T
     */
    private static <T> T syncRequest(OkHttpClient client, Request request, BiFunction<String, String, T> func) {
        boolean isAccessFailed = false;
        do {
            try (Response response = client.newCall(request).execute()) {
                TimeUnit.MILLISECONDS.sleep(REQUEST_DELAY_TIME);
                if (isAccessFailed) isAccessFailed = false;
                if (response.isSuccessful()) {
                    return func.apply(response.body().string(), response.request().url().toString());
                } else
                    throw new IllegalStateException(response.code() + " " + response.message());
            } catch (IOException | InterruptedException e) {
                if (e instanceof InterruptedException || InterruptedIOException.class.equals(e.getClass()))
                    throw new CustomizedInterruptedException(e.getMessage());
                try {
                    TimeUnit.MILLISECONDS.sleep(REQUEST_DELAY_TIME);
                } catch (InterruptedException ex) {
                    throw new CustomizedInterruptedException(e.getMessage());
                }
                LOGGER.info("", e);
                isAccessFailed = true;
            }
        } while (isAccessFailed);
        throw new IllegalStateException("请求: [" + request.url() + "] 获取结果失败!");
    }
}
