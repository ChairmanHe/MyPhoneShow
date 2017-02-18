package com.hjt.phoneshow.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.google.gson.Gson;
import com.hjt.phoneshow.R;
import com.hjt.phoneshow.api.LitePalStores;
import com.hjt.phoneshow.base.BaseActivity;
import com.hjt.phoneshow.bean.NewsBean;
import com.hjt.phoneshow.ui.adapter.LitePalAdapter;
import com.hjt.phoneshow.ui.adapter.SpacesItemDecoration;
import com.hjt.phoneshow.util.CopyDBUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/12/8.
 */
//这是使用litepal对数据库进行操作  具体参考http://blog.csdn.net/guolin_blog/article/details/38556989
//Litepal数据库映射插入到数据库
// 1.在要插入的对象上面继承DataSupport
// 2.在application继承LitePalApplication
// 3.配置导入jar包，在assets目录下新建litepal.xml，指定数据库名字和版本以及映射关系
public class LitePalActivity extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    //网络请求数据
    private LitePalAdapter adapter;
    private String baseUrl = "http://c.m.163.com/";
    public static final String url = "nc/article/headline/T1348647909107/0-20.html";
    private ArrayList<NewsBean> lists;
    //private String url = "http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_litepal;
    }

    @Override
    protected void initParams() {
        try {
            FileOutputStream a = new FileOutputStream(Environment.getExternalStorageDirectory());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //发送请求获取网络数据
//        mCompositeSubscription.add(observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
        //使用rx   rx的笔记   Func1是有返回值的    Action1是没有返回值的 有一个参数就是1
//        Observable.just("images/logo.png") // 输入类型 String
//                .map(new Func1<String, Bitmap>() {
//                    @Override
//                    public Bitmap call(String filePath) { // 参数类型 String
//                        return getBitmapFromPath(filePath); // 返回类型 Bitmap
//                    }
//                })
//                .subscribe(new Action1<Bitmap>() {
//                    @Override
//                    public void call(Bitmap bitmap) { // 参数类型 Bitmap
//                        showBitmap(bitmap);
//                    }
//                });
        //创建一个retrofit对象
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        OkHttpClient okHttpClient = builder.build();
        Retrofit retorfit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(ScalarsConverterFactory.create()).build();
        final LitePalStores litePalStores = retorfit.create(LitePalStores.class);
        //         /*******************************发送请求数据***************************************/
        Call<String> call = litePalStores.loadNewsbean();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String datas = response.body().toString();
                //解析数据
                lists = (ArrayList<NewsBean>) parseJson(datas);
                // 用litepal保存到数据库中
                NewsBean.saveAll(lists);
                //呈现数据在界面上面    这里使用到了瀑布流
               adapter = new LitePalAdapter(lists,LitePalActivity.this);
               recycleView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
               recycleView.setAdapter(adapter);
                SpacesItemDecoration decoration= new SpacesItemDecoration(16);
                recycleView.addItemDecoration(decoration);
                //将数据表拷贝到sd中
                CopyDBUtils.copyDbToSDcard();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("String", t.toString());
            }
        });
//        //使用最简单的方式使用rxjava
//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(final Subscriber<? super String> subscriber) {
//                //使用retorfit发送请求获取数据
////                Call<NewsBean> newsBeanCall = apiStores.loadNewsbean();
////                newsBeanCall.enqueue(new Callback<NewsBean>() {
////                    @Override
////                    public void onResponse(Call<NewsBean> call, Response<NewsBean> response) {
////                        NewsBean body = response.body();
////                        subscriber.onNext(body);
////                    }
////
////                    @Override
////                    public void onFailure(Call<NewsBean> call, Throwable t) {
////
////                    }
////                });
////                long id = Thread.currentThread().getId();
////                Log.e("new","打印前"+id);
////                subscriber.onNext("打印成功");
//
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String newsBean) {
//                Log.i("new",newsBean.toString());
//                long id = Thread.currentThread().getId();
//                Log.e("new","打印后"+id);
//            }
//        });


    }


    // josn数据解析
    private List<NewsBean> parseJson(String response) {
        Gson gson = new Gson();
        ArrayList<NewsBean> beans = new ArrayList<NewsBean>();
        try {
            JSONObject json = new JSONObject(response);
            JSONArray jsonArray = json.getJSONArray("T1348647909107");
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                NewsBean news = gson.fromJson(jo.toString(), NewsBean.class);
                beans.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_deltct:
//                // 根据id删除一条数据
//                DataSupport.delete(NewsBean.class, 7);
//                DataSupport.deleteAll(NewsBean.class, "docid=?", "BV6040M800031H2L");
//                break;
//            case R.id.bt_update:
//                // 修改一条数据，直接使用静态方法
//                // DataSupport.update(Class<?> modelClass, ContentValues values,long
//                // id)
//                ContentValues values = new ContentValues();
//                values.put("title", "李志刚是个傻逼");
//                DataSupport.update(NewsBean.class, values, 4);
//                // 修改多条，直接使用静态方法 DataSupport.updateAll(News.class, values, "title =
//                // ? and commentcount > ?", "xxxxx", "0");
//                // DataSupport.updateAll(modelClass, values, conditions)
//                // 比如约束条件中有一个占位符，那么后面就应该填写一个参数，如果有两个占位符，后面就应该填写两个参数，以此类推
//                ContentValues value = new ContentValues();
//                value.put("digest", "李志刚是个傻逼");
//                DataSupport.updateAll(NewsBean.class, value, "id=?", "4");
//
//                break;
//            case R.id.bt_select:
//                ArrayList<NewsBean> list = (ArrayList<NewsBean>) DataSupport.where("id like ?", "2").find(NewsBean.class);
//                NewsAdapter2 adapter = new NewsAdapter2(LitepalActivity.this, list);
//                mListView.setAdapter(adapter);
//                // 通用的几个查询方式，使用id
//                // NewsBean NewsBean = DataSupport.find(NewsBean.class, 1);
//                // NewsBean firstNewsBean = DataSupport.findFirst(NewsBean.class);
//                // NewsBean lastNewsBean = DataSupport.findLast(NewsBean.class);
//                // List<NewsBean> NewsBeanList = DataSupport.findAll(NewsBean.class,
//                // 1, 3, 5, 7);
//                // List<NewsBean> NewsBeList = DataSupport.findAll(NewsBean.class,
//                // new long[] { 1, 3, 5, 7 });
//                // List<NewsBean> allNewsBean = DataSupport.findAll(NewsBean.class);
//                break;
//
//        }
//
//    }


}
