package com.hjt.phoneshow.ui.presenter.imp;


import com.hjt.phoneshow.base.BasePresenter;
import com.hjt.phoneshow.bean.ModelList;
import com.hjt.phoneshow.ui.model.BaseCallBack;
import com.hjt.phoneshow.ui.model.IMainActivityModel;
import com.hjt.phoneshow.ui.model.imp.MainActivityModel;
import com.hjt.phoneshow.ui.presenter.IMainActivityPresenter;
import com.hjt.phoneshow.ui.view.IMainActivityView;

/**
 * Created by Administrator on 2016/11/22.
 */

public class MainActivityPresenter extends BasePresenter implements IMainActivityPresenter {
    public IMainActivityView view;
    public IMainActivityModel model;

    public MainActivityPresenter(IMainActivityView view) {
        this.view = view;
        this.model = new MainActivityModel();
    }

    @Override
    public void getData(int type, String paramId) {
        model.getdata(type, paramId, new BaseCallBack() {
            @Override
            public void success(Object obj) {
                ModelList modelList = (ModelList) obj;
                view.getDataSuccess(modelList);
            }

            @Override
            public void failure(String fail_msg) {
              view.getDataFail(fail_msg);
            }

            @Override
            public void finish() {
                view.getfinish();
            }

            @Override
            public void noWaln(int msg) {
                view.noWaln(msg);
            }
        });
    }

    //测试用
//    @Override
//    public void loadData(String cityId) {
//        view.showLoading();
//        addSubscription(apiStores.loadDataByRetrofitRxjava(cityId),
//                new ApiCallback<MainModel>() {
//                    @Override
//                    public void onSuccess(MainModel model) {
//                        view.getDataSuccess(model);
//                    }
//
//                    @Override
//                    public void onFailure(String msg) {
//                        view.getDataFail(msg);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        view.hideLoading();
//                    }
//                });
//    }


}
