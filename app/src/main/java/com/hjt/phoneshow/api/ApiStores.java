package com.hjt.phoneshow.api;


import com.hjt.phoneshow.constant.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiStores {
    //baseUrl
//    String API_SERVER_URL = "http://www.weather.com.cn/";

//    //加载天气
//    @GET("adat/sk/{cityId}.html")
//    Call<MainModel> loadDataByRetrofit(@Path("cityId") String cityId);
//
//    //加载天气
//    @GET("adat/sk/{cityId}.html")
//    Observable<MainModel> loadDataByRetrofitRxjava(@Path("cityId") String cityId);
//baseUrl
  String API_SERVER_URL = "http://api.kxdcloud.com/";
    //test
   // String API_SERVER_URL = "http://logcollect.kxdos.com/";
    //加载手机秀主界面的图片数据
    @FormUrlEncoded
    @POST(Constant.MODELLIST)
    Observable<String> loadDataDetail(@Field("data") String data);

    //加载手机秀详情界面的额图片数据
    @FormUrlEncoded
    @POST(Constant.MODELDETAIL)
    Observable<String> loadData(@Field("data") String data);

}
