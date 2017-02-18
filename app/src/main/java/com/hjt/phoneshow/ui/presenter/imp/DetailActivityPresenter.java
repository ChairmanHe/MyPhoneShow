package com.hjt.phoneshow.ui.presenter.imp;


import com.hjt.phoneshow.base.BasePresenter;
import com.hjt.phoneshow.bean.Detail;
import com.hjt.phoneshow.ui.model.BaseCallBack;
import com.hjt.phoneshow.ui.model.imp.DeatilActivityModel;
import com.hjt.phoneshow.ui.presenter.IDetailActivityPresenter;
import com.hjt.phoneshow.ui.view.IDetailActivityView;

/**
 * Created by Administrator on 2016/11/22.
 */

public class DetailActivityPresenter extends BasePresenter implements IDetailActivityPresenter {
    private IDetailActivityView view;
    private DeatilActivityModel model;

    public DetailActivityPresenter(IDetailActivityView view) {
        this.view = view;
        this.model = new DeatilActivityModel();
    }

    @Override
    public void getDetailData(String id) {
        model.getdata(id, new BaseCallBack() {
            @Override
            public void success(Object obj) {
                view.getDataSuccess((Detail)obj);
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


}
