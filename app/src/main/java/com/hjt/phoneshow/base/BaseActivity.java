package com.hjt.phoneshow.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.hjt.phoneshow.R;
import com.hjt.phoneshow.api.ApiStores;
import com.hjt.phoneshow.api.AppClient;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/11/22.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Activity mActivity;
    private CompositeSubscription mCompositeSubscription;
    private List<Call> calls;
    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(getLayoutId());
        }catch (Exception e){
            e.printStackTrace();
        }
        ButterKnife.bind(this);
        mActivity = this;
        initParams();
    }

    protected abstract void initParams();

    protected abstract int getLayoutId();

//    @Override
//    public void setContentView(@LayoutRes int layoutResID) {
//        super.setContentView(layoutResID);
//        ButterKnife.bind(this);
//        mActivity = this;
//    }

    @Override
    protected void onDestroy() {
       // callCancel();
        onUnsubscribe();
        super.onDestroy();
    }

//    public void setupSwipeRefresh(SwipeRefreshLayout mRefreshLayout){
//        if(mRefreshLayout != null){
//            mRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1,
//                    R.color.refresh_progress_2,R.color.refresh_progress_3);
//            mRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
//                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,getResources().getDisplayMetrics()));
//        }
//    }

/*********************************设置rx的基类方法*************************************************/
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void onUnsubscribe() {
       // LogUtil.d("onUnsubscribe");
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions())
            mCompositeSubscription.unsubscribe();
    }

    //给toolbar设置标题
    public Toolbar initToolBarAsHome(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
//            toolbar.setTitleTextColor(Color.WHITE);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//           actionBar.setDisplayShowTitleEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_menu);
        return toolbar;
    }


    public Toolbar initToolBarAsHome() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
//            toolbar.setTitleTextColor(Color.WHITE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//           actionBar.setDisplayShowTitleEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        return toolbar;
    }

//    /**
//     * 设置toolbar的菜单
//     * @param menu
//     * @return
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
//        return true;
//    }
    /**
     * 判空 返回true，就说明有EditText未输入内容 返回false，就说明EditText都输入了
     */
    public boolean isEmpty(String... ets) {
        for (String editText : ets) {
            if (ets.equals("")) {
                // 如果直接为setError方法提供Stirng类型参数
                // 有可能出现的提示文字使用的颜色，与错误提示框的背景色重复
                // 造成提示文字不可见
                // 但是，setError接受的参数类型是CharSequence类型
                // 所以，更换一下参数的类型，不是用标准的Stirng，而是使用安卓提供的
                // 可扩展String，为可扩展Stirng指定一个不同于提示框背景的颜色
                // <font color="颜色值">
                // editText.setError(Html.fromHtml("<font
                // color=#ff0000>请输入完整</font>"));
                return true;
            }
        }
        return false;
    }
    //***********************************toast和show方法******************************************//
    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public ProgressDialog progressDialog;

    public ProgressDialog showProgressDialog() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("加载中");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }

    public ProgressDialog showProgressDialog(CharSequence message) {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }
    }


}
