package com.example.charge.api.utils;

import com.example.charge.api.ApiException;
import com.example.charge.api.enums.ResponseEnum;
import com.example.charge.entity.DataResponse;
import com.example.charge.entity.MessageResponse;
import com.example.charge.utils.LogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseParser {
    /**
     * Auto-generated: the log tag
     */
    private static final String LOG_TAG = ResponseParser.class.getName();

    public static MessageResponse parseMsgResponse(Response response) throws ApiException {
        if (response.code() != 200) {
            String errLog = "response.code() == " + response.code();
            LogUtils.e(LOG_TAG, errLog);
            throw new ApiException(errLog);
        }
        // get response body
        ResponseBody responseBody = response.body();
        // empty response body
        if (responseBody == null) {
            String errLog = "response.body() == null";
            LogUtils.e(LOG_TAG, errLog);
            throw new ApiException(errLog);
        }
        // Deserialize JSON string to object
        String json = null;
        try {
            json = responseBody.string();
        } catch (IOException e) {
            throw new ApiException(e);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, MessageResponse.class);
        } catch (JsonProcessingException e) {
            throw new ApiException(e);
        }
    }

    public static <T> DataResponse<T> parseDataResponse(Response response) throws ApiException {
        if (response.code() != 200) {
            throw new ApiException("response.code() == " + response.code());
        }
        // get response body
        ResponseBody responseBody = response.body();
        // empty response body
        if (responseBody == null) {
            throw new ApiException("response.body() == null");
        }
        // Deserialize JSON string to object
        String json = null;
        try {
            json = responseBody.string();
        } catch (IOException e) {
            throw new ApiException(e);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<DataResponse<T>>(){});
        } catch (JsonProcessingException e) {
            throw new ApiException(e);
        }
    }
}
