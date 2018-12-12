package com.rastargame.rick.read.model.http.response;

public class MyHttpResponse<T> {
    private int res;
    private T data;

    public MyHttpResponse(int res, T data) {
        this.res = res;
        this.data = data;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
