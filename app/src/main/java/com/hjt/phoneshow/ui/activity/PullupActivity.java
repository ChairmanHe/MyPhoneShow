package com.hjt.phoneshow.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.CircularArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.hjt.phoneshow.R;
import com.hjt.phoneshow.base.BaseLoadingAdapter;
import com.hjt.phoneshow.bean.DesignItem;
import com.hjt.phoneshow.ui.adapter.DesignLoaderMoreAdapter;

/**
 * Created by Administrator on 2016/12/12.
 */

public class PullupActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DesignLoaderMoreAdapter mDesignLoaderMoreAdapter;
    private CircularArray<DesignItem> mDatas;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_loading);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView_loading);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        //ToolbarUtils.show(LoadingMoreActivity.this, mToolbar, true);

        mDatas = new CircularArray<>();

        for (int i = 0; i < 25; i++) {
            mDatas.addLast(new DesignItem("" + i, "" + i));
        }

        /*mLayoutManager = new LinearLayoutManager(
                LoadingMoreActivity.this, LinearLayoutManager.VERTICAL, false);*/

        /*mLayoutManager = new GridLayoutManager(
                LoadingMoreActivity.this, 3, GridLayoutManager.VERTICAL, false);*/

        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        ((StaggeredGridLayoutManager)mLayoutManager).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mDesignLoaderMoreAdapter = new DesignLoaderMoreAdapter(mRecyclerView, mDatas);

        mRecyclerView.setAdapter(mDesignLoaderMoreAdapter);

        mDesignLoaderMoreAdapter.setOnLoadingListener(new BaseLoadingAdapter.OnLoadingListener() {
            @Override
            public void loading() {
                new LoadAsyncTask().execute();
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RefreshAsyncTask().execute();
            }
        });
    }

    private class LoadAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mDesignLoaderMoreAdapter.setLoadingComplete();

            int size = mDatas.size();
            for (int i = size + 1; i < size + 10; i++) {
                mDatas.addLast(new DesignItem("" + i, i + ""));
            }
           // mDesignLoaderMoreAdapter.notifyDataSetChanged();
            mDesignLoaderMoreAdapter.notifyItemRangeInserted(mDatas.size() - 10, 10);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class RefreshAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mSwipeRefresh.setRefreshing(false);
        }
    }

}
