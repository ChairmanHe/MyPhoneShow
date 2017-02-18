package com.hjt.phoneshow.ui.model;

/**
 * Created by Administrator on 2016/11/23.
 */

public interface BaseCallBack {
    /**
     * 成功返回数据
     * @param obj
     */
    public void  success(Object obj);

    /**
     * 失败返回失败的数据
     * @param fail_msg
     */
    public void  failure(String fail_msg);

    /**
     * 请求完成后
     */
    public void  finish();
    /**
     * 没有网络
     */
    public void  noWaln(int msg);
}
