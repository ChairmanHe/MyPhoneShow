package com.hjt.phoneshow.ui.model.imp;

import android.text.TextUtils;
import android.util.Log;

import com.hjt.phoneshow.api.ApiCallback;
import com.hjt.phoneshow.base.BaseModel;
import com.hjt.phoneshow.bean.Detail;
import com.hjt.phoneshow.constant.Constant;
import com.hjt.phoneshow.ui.model.BaseCallBack;
import com.hjt.phoneshow.ui.model.IDetailActivityModel;
import com.hjt.phoneshow.util.CacheUtil;
import com.hjt.phoneshow.util.JsonUtil;
import com.hjt.phoneshow.util.Util;
import com.hjt.phoneshow.util.desutil.DesUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/23.
 */

public class DeatilActivityModel extends BaseModel implements IDetailActivityModel {

    @Override
    public void getdata(final String id, final BaseCallBack callBack) {
        //读取缓存中的数据
        String cache = CacheUtil.getDetail(id);
        if(!TextUtils.isEmpty(cache)){
            Log.e("detail","cache"+cache.toString());
            Detail detail = JsonUtil.getJsonUtil().fromJson(cache, Detail.class);
            if(detail!=null){
                callBack.success(detail);
            }else {
                callBack.failure("nodata");
            }

        }else{
            if(Util.getNetworkType() != Util.NetworkType.NONE){
                //给数据加密
                String requestData = getparameter(id);
                addSubscription(apiStores.loadData(requestData), new ApiCallback<String>() {
                    @Override
                    public void onSuccess(String model) {
                       //TODO 请求成功后会返回结果
                        Log.i("resullt",model.toString());
                        //解密数据
                        String responsData = DesUtil.getDesUtil().decrypt(model);
                        Log.e("detail","responsData"+responsData);
                        //保存解密后的数据
                        CacheUtil.saveDetail(responsData,id);
                        Detail detail = JsonUtil.getJsonUtil().fromJson(responsData, Detail.class);
                        if(detail!=null){
                            callBack.success(detail);
                        }else {
                            callBack.failure("nodata");
                        }

                    }

                    @Override
                    public void onFailure(String msg) {
                        callBack.failure(msg);
                    }

                    @Override
                    public void onFinish() {

                    }
                });

            }else {
                //没有网络
                callBack.noWaln(Constant.NONETANDHASCACHE);
            }
        }

    }

    /**
     * 封装请求参数
     * @param id 传来数据ID
     * @return
     */
    private String getparameter(String id) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("site_code", "200007");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //给请求数据加密
        String encrypt = DesUtil.getDesUtil().encrypt(obj.toString());
        return encrypt;
    }

}
