package com.hjt.phoneshow.api;

import com.hjt.phoneshow.ui.activity.LitePalActivity;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2016/12/21.
 */

public interface LitePalStores {
    //retorfit的简单使用  加载新闻的入口
    @GET(LitePalActivity.url)
    Call<String> loadNewsbean();
}
