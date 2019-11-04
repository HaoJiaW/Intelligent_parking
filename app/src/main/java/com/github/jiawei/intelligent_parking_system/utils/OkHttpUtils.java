package com.github.jiawei.intelligent_parking_system.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    private OkHttpClient client;
    private Request request;
    private Response response;

    public Response requestMethod(String url, RequestBody body)  {
        client =new OkHttpClient();
        if (body!=null){
            request =new Request.Builder().url(url).post(body).build();
        }else {
        request =new Request.Builder().url(url).build();
        }
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
