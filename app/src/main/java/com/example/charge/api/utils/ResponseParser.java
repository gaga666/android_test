package com.example.charge.api.utils;

import com.example.charge.api.exception.ApiException;
import com.example.charge.api.model.DataResponse;
import com.example.charge.api.model.MessageResponse;
import com.example.charge.utils.LogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.reflect.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseParser {
    /**
     * Auto-generated: the log tag
     */
    private static final String LOG_TAG = ResponseParser.class.getName();

    public static MessageResponse parseMsgResponse(Response response) throws ApiException {
        if (response == null) {
            throw new ApiException("response == null");
        }
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

    public static <T> DataResponse<T> parseDataResponse(Response response, Class<T> cls) throws ApiException {
        if (response == null) {
            throw new ApiException("response == null");
        }
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
            return mapper.readValue(json, new TypeReference<DataResponse<T>>() {
                @Override
                public Type getType() {
                    return TypeUtils.parameterize(DataResponse.class, cls);
                }
            });
        } catch (JsonProcessingException e) {
            throw new ApiException(e);
        }
    }
}
