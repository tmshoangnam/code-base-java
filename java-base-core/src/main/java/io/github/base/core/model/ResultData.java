package io.github.base.core.model;

/**
 * Standard response wrapper for all API endpoints.
 *
 * <p>This class provides a consistent response structure across all APIs,
 * ensuring clients receive predictable response formats with proper error handling.
 *
 * @param <T> the type of data in the response
 * @since 1.0.0
 * @author java-base-core
 */
public class ResultData<T> {
    private int code;      // HTTP status code
    private String msg;    // message
    private T data;        // response data

    public ResultData() { /* TODO document why this constructor is empty */ }

    public int getCode() {
        return code;
    }

    public ResultData<T> setCode(int resultCode) {
        this.code = resultCode;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultData<T> setData(T data) {
        this.data = data;
        return this;
    }
}
