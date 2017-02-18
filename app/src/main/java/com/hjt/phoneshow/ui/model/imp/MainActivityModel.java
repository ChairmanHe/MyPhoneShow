package com.hjt.phoneshow.ui.model.imp;

import android.text.TextUtils;

import com.hjt.phoneshow.api.ApiCallback;
import com.hjt.phoneshow.base.BaseModel;
import com.hjt.phoneshow.bean.ModelList;
import com.hjt.phoneshow.constant.Constant;
import com.hjt.phoneshow.ui.model.BaseCallBack;
import com.hjt.phoneshow.ui.model.IMainActivityModel;
import com.hjt.phoneshow.util.CacheUtil;
import com.hjt.phoneshow.util.JsonUtil;
import com.hjt.phoneshow.util.Util;
import com.hjt.phoneshow.util.desutil.DesUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/23.
 */

public class MainActivityModel extends BaseModel implements IMainActivityModel {
    private int loadMore[] = {0, 0};

    //这里通过回调将数据传给presenter层
    @Override
    public void getdata(final int pageType, final String paramId, final BaseCallBack callBack) {
        //这里是为了表示第一次进来的时候无论怎样都是向下查询
        if (loadMore[pageType] == 0) {
            if ("0".equals(paramId)) {
                loadMore[1] = 1;
            }
        }
        //读取缓存中的数据
        String cache = CacheUtil.getModel(paramId, pageType);
        //读取缓存没有并且没有网络
        if (!TextUtils.isEmpty(cache)) {
            ModelList modelList = JsonUtil.getJsonUtil().fromJson(cache, ModelList.class);
            callBack.success(modelList);
        } else {
            if ((Util.getNetworkType() != Util.NetworkType.NONE)) {
                String requestData = getparameter(pageType, paramId);
                //发送网络请求获取数据   这里的apicallback里面的参数是请求结果返回来的数据类型
                addSubscription(apiStores.loadDataDetail(requestData), new ApiCallback<String>() {
                    @Override
                    public void onSuccess(String model) {
                        //解密数据
                        String tp = DesUtil.getDesUtil().decrypt(model);
                        ModelList modelList = JsonUtil.getJsonUtil().fromJson(tp, ModelList.class);
                        if (modelList.getStatus() == 0) {
                            ////返回的数据是空不能保存 保存解密后的数据
                            if(modelList.getData().get(0).getList().size()!=0){
                                CacheUtil.saveModel(tp, paramId, pageType);
                            }
                            //发送成功数据
                            callBack.success(modelList);
                        } else {
                            callBack.failure("网络异常");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        callBack.failure(msg);
                    }

                    @Override
                    public void onFinish() {
                        callBack.finish();
                    }
                });

            } else {
                callBack.noWaln(Constant.NONETANDHASCACHE);
            }
        }

    }

    /**
     * 封装请求参数
     *
     * @param pageType
     * @param maxId
     * @return
     */
    private String getparameter(int pageType, String maxId) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("max_id", maxId);
            obj.put("page_type", String.valueOf(pageType));
            obj.put("page_size", "10");
            obj.put("site_code", "200007");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //给请求数据加密
        String encrypt = DesUtil.getDesUtil().encrypt(obj.toString());
        return encrypt;
    }

}
