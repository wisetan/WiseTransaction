package com.wise.transdemo.base;

import java.util.List;

public class ResponseFactory {

    public static <T> CommonResponse<T> createCommonResponse(int code, String message, T data) {
        return new CommonResponse<>(code, message, data);
    }

    public static <T> PageResponse<T> createPageResponse(int code, String message, List<T> data, int page, int size, int totalPages, long totalElements) {
        return new PageResponse<>(code, message, data, page, size, totalPages, totalElements);
    }
}