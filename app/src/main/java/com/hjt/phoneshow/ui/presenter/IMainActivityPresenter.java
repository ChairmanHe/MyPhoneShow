package com.hjt.phoneshow.ui.presenter;

/**
 * Created by Administrator on 2016/11/22.
 */

public interface IMainActivityPresenter {
    /**
     * c传入一个参数来加载数据
     * @param cityId
     */
  //  public void loadData(String cityId);

    /**
     * 获取手机秀机型列表
     */
    public void getData(int type, String paramId);

}
