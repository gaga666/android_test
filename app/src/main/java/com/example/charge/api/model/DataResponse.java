package com.example.charge.api.model;


public class DataResponse<T> extends MessageResponse {

    private T data;

    public DataResponse() {}

    public DataResponse(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
