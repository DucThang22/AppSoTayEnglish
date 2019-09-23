package com.example.appsotayenglish.data.itf;

import com.example.appsotayenglish.data.model.api.CategoryResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("/api/v1/get-all-category")
    Call<CategoryResponse> showCategory(@Query("language_code") String language_code);

}
