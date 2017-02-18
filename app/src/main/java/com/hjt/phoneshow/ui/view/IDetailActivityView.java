package com.hjt.phoneshow.ui.view;


import com.hjt.phoneshow.bean.Detail;

/**
 * Created by Administrator on 2016/11/22.
 */

public interface IDetailActivityView extends BaseView  {
    /**
     * 数据加载成功后的回调
     * @param data
     */
    void getDataSuccess(Detail data);

    /**
     * 数据加载失败后的回调
     * @param msg
     */
    void getDataFail(String msg);
    /**
     * 加载数据完成后
     */
    void getfinish();
    /**
     * 没有网络
     */
    void noWaln(int msg);

}
