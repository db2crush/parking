package com.yalo.erunn.parking;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by erunn on 2017-06-04.
 */

public interface ApiService {
    public static final String ApiURL = "http://220.230.119.61:3000/api/";

    @GET("parks")
    Call<ResponseBody> getParks();


}
