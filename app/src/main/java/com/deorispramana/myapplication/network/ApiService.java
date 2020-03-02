package com.deorispramana.myapplication.network;

import com.deorispramana.myapplication.model.Response;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @Multipart
    @POST("store/recorder")
    Call<Response> postData(
            @Part MultipartBody.Part file
    );
}
