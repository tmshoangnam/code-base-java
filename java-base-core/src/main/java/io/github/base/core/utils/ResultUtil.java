package io.github.base.core.utils;

import io.github.base.core.model.ResultData;

import java.util.Collection;

/**
 * Utility class for creating standardized API responses.
 *
 * <p>This class provides static methods to create consistent ResultData responses
 * following MDC standards for all API endpoints.
 *
 * @author fg-dev
 */
public class ResultUtil {

    /**
     * Standard success message.
     */
    private static final String SUCCESS = "Success";

    private ResultUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a successful response with no data.
     *
     * @param <T> the type of data
     * @return ResultData with code 200 and success message
     */
    public static <T> ResultData<T> success() {
        return new ResultData<T>().setCode(200).setMsg(SUCCESS);
    }

    /**
     * Creates a successful response with data.
     *
     * @param data the data to return
     * @param <T> the type of data
     * @return ResultData with code 200, success message, and data
     */
    public static <T> ResultData<T> success(T data) {
        return new ResultData<T>().setCode(200).setMsg(SUCCESS).setData(data);
    }

    /**
     * Creates an accepted response with data.
     *
     * @param data the data to return
     * @param <T> the type of data
     * @return ResultData with code 202, success message, and data
     */
    public static <T> ResultData<T> successAccepted(T data) {
        return new ResultData<T>().setCode(202).setMsg(SUCCESS).setData(data);
    }

    /**
     * Creates an accepted response with data.
     *
     * @param data the data to return
     * @param <T> the type of data
     * @return ResultData with code 202 and accepted message
     */
    public static <T> ResultData<T> accepted(T data) {
        return new ResultData<T>().setCode(202).setMsg(SUCCESS).setData(data);
    }

    /**
     * Creates an error response with message.
     *
     * @param message the error message
     * @param <T> the type of data
     * @return ResultData with code 400 and error message
     */
    public static <T> ResultData<T> error(String message) {
        return new ResultData<T>().setCode(400).setMsg(message);
    }

    /**
     * Creates a response based on data content.
     * Automatically checks for empty/null data and returns appropriate response.
     *
     * @param data the data to check and return
     * @param <T> the type of data
     * @return ResultData with appropriate code based on data content
     */
    public static <T> ResultData<T> resultData(T data) {
        if (data instanceof Collection<?> collection) {
            if (CollectionUtils.isEmpty(collection)) {
                return new ResultData<T>().setCode(400).setMsg("data is empty");
            } else {
                return new ResultData<T>().setCode(200).setMsg(SUCCESS).setData(data);
            }
        } else if (data == null) {
            return new ResultData<T>().setCode(400).setMsg("data is null");
        } else {
            return new ResultData<T>().setCode(200).setMsg(SUCCESS).setData(data);
        }
    }

    /**
     * Creates a custom response with status code and message.
     *
     * @param statusCode the HTTP status code
     * @param message the response message
     * @param <T> the type of data
     * @return ResultData with custom status code and message
     */
    public static <T> ResultData<T> resultData(int statusCode, String message) {
        return new ResultData<T>().setCode(statusCode).setMsg(message);
    }

    /**
     * Creates a custom response with status code, message, and data.
     *
     * @param statusCode the HTTP status code
     * @param message the response message
     * @param data the data to return
     * @param <T> the type of data
     * @return ResultData with custom status code, message, and data
     */
    public static <T> ResultData<T> resultData(int statusCode, String message, T data) {
        ResultData<T> result = new ResultData<T>().setCode(statusCode).setMsg(message);
        result.setData(data);
        return result;
    }
}
